package com.mansaeng.diridibackend.repository

import com.mansaeng.diridibackend.entity.article.Episode
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface EpisodeRepository : ReactiveMongoRepository<Episode, String> {
    @Query("{ id: { \$exists: true }, articleId: ?0 }")
    fun findAllPaged(articleId: String, pageable: Pageable): Flux<Episode>

    fun findByArticleIdAndPublishedAtBefore(
        articleId: String,
        publishedAt: LocalDateTime,
        pageable: Pageable
    ): Flux<Episode>
}