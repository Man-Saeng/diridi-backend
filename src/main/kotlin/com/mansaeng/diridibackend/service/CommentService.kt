package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.request.CreateCommentRequest
import com.mansaeng.diridibackend.entity.comment.Comment
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.repository.CommentRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CommentService(private val commentRepository: CommentRepository) {

    fun createComment(writer: User, createCommentRequest: CreateCommentRequest): Mono<String> {
        val (articleId, parentId, content) = createCommentRequest

        val newComment = Comment(writerId = writer.id, parentId = parentId, content = content, articleId = articleId)
        return commentRepository.save(newComment).mapNotNull { it.id }
    }

    fun getCommentsByArticleId(articleId: String, parentId: String?): Flux<Comment> {
        return if (parentId == null) commentRepository.findByArticleIdAndParentIdNull(articleId)
        else commentRepository.findByArticleIdAndParentId(
            articleId,
            parentId
        )
    }
}