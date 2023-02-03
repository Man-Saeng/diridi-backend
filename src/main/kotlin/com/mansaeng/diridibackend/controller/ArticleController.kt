package com.mansaeng.diridibackend.controller

import com.mansaeng.diridibackend.dto.CreateArticleRequest
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.service.ArticleService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/article")
class ArticleController(private val articleService: ArticleService) {

    @PostMapping
    fun createArticle(
        principal: Principal,
        @RequestBody createArticleRequest: CreateArticleRequest
    ): Mono<ResponseEntity<String>> = articleService.createArticle(
        (principal as UsernamePasswordAuthenticationToken).principal as User,
        createArticleRequest
    ).mapNotNull { article -> ResponseEntity.ok(article.id) }
}