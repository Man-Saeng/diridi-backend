package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.CreateEpisodeRequest
import com.mansaeng.diridibackend.entity.article.Episode
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.repository.ArticleRepository
import com.mansaeng.diridibackend.repository.EpisodeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

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

        return episodeRepository.findAllPaged(articleId, page)
    }

}