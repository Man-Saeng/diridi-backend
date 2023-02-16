package com.mansaeng.diridibackend.repository

import com.mansaeng.diridibackend.entity.article.Article
import com.mansaeng.diridibackend.entity.article.StatusType
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux


interface ArticleRepository : ReactiveCrudRepository<Article, String> {
    @Query("{ id: { \$exists: true }}")
    fun findAllPaged(pageable: Pageable): Flux<Article>

    fun findByStatusIsNot(status: StatusType, pageable: Pageable): Flux<Article>

    fun findByTagsContainingAndStatusIsNot(tag: String?, status: StatusType, pageable: Pageable): Flux<Article>
}
