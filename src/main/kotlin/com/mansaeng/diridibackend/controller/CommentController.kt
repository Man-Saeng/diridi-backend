package com.mansaeng.diridibackend.controller

import com.mansaeng.diridibackend.dto.request.CreateCommentRequest
import com.mansaeng.diridibackend.entity.comment.Comment
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.service.CommentService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/comment")
class CommentController(private val commentService: CommentService) {
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    fun createComment(principal: Principal, @RequestBody createCommentRequest: CreateCommentRequest): Mono<String> {
        return commentService.createComment(
            (principal as UsernamePasswordAuthenticationToken).principal as User,
            createCommentRequest
        )
    }

    @GetMapping
    fun getComments(
        @RequestParam(name = "articleId", required = true) articleId: String,
        @RequestParam(name = "parentId") parentId: String?
    ): Flux<Comment> {
        return commentService.getCommentsByArticleId(articleId, parentId)
    }
}