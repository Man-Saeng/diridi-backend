package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.request.CreateEpisodeRequest
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
}
