package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.request.CreateEpisodeRequest
import com.mansaeng.diridibackend.dto.request.LikeEpisodeRequest
import com.mansaeng.diridibackend.dto.request.ModifyEpisodeRequest
import com.mansaeng.diridibackend.entity.article.Episode
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.repository.ArticleRepository
import com.mansaeng.diridibackend.repository.EpisodeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.LocalDateTime

@Service
class EpisodeService(
    private val articleRepository: ArticleRepository,
    private val episodeRepository: EpisodeRepository
) {
    fun createEpisode(writer: User, createEpisodeRequest: CreateEpisodeRequest): Mono<Episode> {
        val (title, content, publishedAt, articleId) = createEpisodeRequest
        val episode = Episode(
            title = title,
            content = content,
            publishedAt = publishedAt,
            articleId = articleId,
            writerId = writer.id
        )

        return episodeRepository.save(episode)
            .publishOn(Schedulers.boundedElastic())
            .map { savedEpisode ->
                articleRepository.findById(articleId).flatMap {
                    it.episodeIds = it.episodeIds + (savedEpisode.id as String)
                    articleRepository.save(it)
                }.subscribe()
                savedEpisode
            }
    }

    fun getEpisodeListByArticleId(articleId: String, skip: Int, take: Int): Flux<Episode> {
        val page = PageRequest.of(skip, take)
        println(articleId)
        return episodeRepository.findByArticleIdAndPublishedAtBefore(articleId, LocalDateTime.now(), page)
    }

    fun getEpisodeDetail(episodeId: String): Mono<Episode> {
        return episodeRepository.findById(episodeId)
    }

    fun modifyEpisode(writer: User, modifyEpisodeRequest: ModifyEpisodeRequest): Mono<Episode> {
        val (episodeId, title, content, publishedAt) = modifyEpisodeRequest
        return episodeRepository.findById(episodeId)
            .map { episode ->
                if (episode.writerId != writer.id) throw RuntimeException("자신이 쓴 게시물만 수정할 수 있습니다")
                episode
            }.map { episode ->
                if (title != null) episode.title = title
                if (content != null) episode.content = content
                if (publishedAt != null) episode.publishedAt = publishedAt
                episode
            }.flatMap { episode ->
                episodeRepository.save(episode)
            }.switchIfEmpty(Mono.error(RuntimeException("존재하지 않는 episode id 입니다")))
    }

    fun likeEpisode(user: User, episodeId: String, likeEpisodeRequest: LikeEpisodeRequest): Mono<Boolean> {
        val (like) = likeEpisodeRequest

        return episodeRepository.findById(episodeId)
            .mapNotNull { it }
            .switchIfEmpty(Mono.error(RuntimeException("Episode ID를 확인해주세요")))
            .flatMap {
                it as Episode
                val likedAlready = it.likedUsers.indexOf(user.id) != -1
                if (like && !likedAlready) {
                    // 좋아요를 한 적이 없는데, 좋아요를 한 상황
                    val mutableLikedUsers = it.likedUsers.toMutableList()
                    mutableLikedUsers.add(user.id)
                    it.likedUsers = mutableLikedUsers
                    return@flatMap episodeRepository.save(it)
                } else if (!like && likedAlready) {
                    // 좋아요를 한 적이 있는데 좋아요를 취소했을 때
                    val mutableLikedUsers = it.likedUsers.toMutableList()
                    mutableLikedUsers.remove(user.id)
                    it.likedUsers = mutableLikedUsers
                    return@flatMap episodeRepository.save(it)
                } else {
                    Mono.error(RuntimeException("이미 좋아요를 했거나, 좋아요를 취소했습니다."))
                }
            }
            .map { like }
    }
}
