package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.request.CreateCommentRequest
import com.mansaeng.diridibackend.dto.request.LikeCommentRequest
import com.mansaeng.diridibackend.entity.comment.Comment
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.repository.ArticleRepository
import com.mansaeng.diridibackend.repository.CommentRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository
) {

    fun createComment(writer: User, createCommentRequest: CreateCommentRequest): Mono<String> {
        val (articleId, parentId, content) = createCommentRequest

        return articleRepository.findById(articleId).switchIfEmpty(Mono.error(RuntimeException("No such article")))
            .flatMap { _ ->
                val newComment =
                    Comment(writerId = writer.id, parentId = parentId, content = content, articleId = articleId)
                commentRepository.save(newComment).mapNotNull { it.id }
            }
    }

    fun getCommentsByArticleId(articleId: String, parentId: String?): Flux<Comment> {
        return if (parentId == null) commentRepository.findByArticleIdAndParentIdNull(articleId)
        else commentRepository.findByArticleIdAndParentId(
            articleId,
            parentId
        )
    }

    fun likeComment(user: User, commentId: String, likeCommentRequest: LikeCommentRequest): Mono<Boolean> {
        val (like) = likeCommentRequest

        return commentRepository.findById(commentId)
            .mapNotNull { it }
            .switchIfEmpty(Mono.error(RuntimeException("Comment ID를 확인해주세요")))
            .flatMap {
                it as Comment
                val likedAlready = it.likedUsers.indexOf(user.id) != -1
                if (like && !likedAlready) {
                    // 좋아요를 한 적이 없는데, 좋아요를 한 상황
                    val mutableLikedUsers = it.likedUsers.toMutableList()
                    mutableLikedUsers.add(user.id)
                    it.likedUsers = mutableLikedUsers
                    return@flatMap commentRepository.save(it)
                } else if (!like && likedAlready) {
                    // 좋아요를 한 적이 있는데 좋아요를 취소했을 때
                    val mutableLikedUsers = it.likedUsers.toMutableList()
                    mutableLikedUsers.remove(user.id)
                    it.likedUsers = mutableLikedUsers
                    return@flatMap commentRepository.save(it)
                } else {
                    Mono.error(RuntimeException("이미 좋아요를 했거나, 좋아요를 취소했습니다."))
                }
            }
            .map { like }
    }
}