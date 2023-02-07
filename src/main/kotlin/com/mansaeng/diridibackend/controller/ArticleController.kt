package com.mansaeng.diridibackend.controller

import com.mansaeng.diridibackend.dto.CreateArticleRequest
import com.mansaeng.diridibackend.entity.article.Article
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.service.ArticleService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/article")
class ArticleController(private val articleService: ArticleService) {

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    fun createArticle(
        principal: Principal,
        @RequestBody createArticleRequest: CreateArticleRequest
    ): Mono<ResponseEntity<String>> = articleService.createArticle(
        (principal as UsernamePasswordAuthenticationToken).principal as User,
        createArticleRequest
    ).mapNotNull { article -> ResponseEntity.ok(article.id) }

    @GetMapping
    fun getArticleList(
        @RequestParam("tag") tag: String?,
        @RequestParam("page") skip: Int?,
        @RequestParam("take") take: Int?
    ): ResponseEntity<Flux<Article>> = ResponseEntity.ok(articleService.getArticleList(tag, skip ?: 0, take ?: 10))

    @GetMapping("/{articleId}")
    fun getArticleDetail(
        @PathVariable articleId: String
    ): ResponseEntity<Mono<Article>> = ResponseEntity.ok(articleService.getArticleDetailById(articleId))
}