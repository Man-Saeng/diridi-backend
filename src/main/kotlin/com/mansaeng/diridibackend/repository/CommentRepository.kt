package com.mansaeng.diridibackend.repository

import com.mansaeng.diridibackend.entity.comment.Comment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface CommentRepository : ReactiveMongoRepository<Comment, String> {
    fun findByArticleId(articleId: String): Flux<Comment>

    fun findByArticleIdAndParentIdNull(articleId: String): Flux<Comment>

    fun findByArticleIdAndParentId(articleId: String, parentId: String): Flux<Comment>
}
