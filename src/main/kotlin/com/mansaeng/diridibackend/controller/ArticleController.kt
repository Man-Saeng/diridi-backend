package com.mansaeng.diridibackend.controller

import com.mansaeng.diridibackend.dto.request.CreateArticleRequest
import com.mansaeng.diridibackend.dto.request.LikeArticleRequest
import com.mansaeng.diridibackend.entity.article.Article
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.service.ArticleService
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
    ): Mono<String> = articleService.createArticle(
        (principal as UsernamePasswordAuthenticationToken).principal as User,
        createArticleRequest
    ).map { article -> article.id }

    @GetMapping
    fun getArticleList(
        @RequestParam("tag") tag: String?,
        @RequestParam("page") skip: Int?,
        @RequestParam("take") take: Int?
    ): Flux<Article> = articleService.getArticleList(tag, skip ?: 0, take ?: 10)

    @GetMapping("/{articleId}")
    fun getArticleDetail(
        @PathVariable articleId: String
    ): Mono<Article> = articleService.getArticleDetailById(articleId)

    @GetMapping("/user/{writerId}")
    fun getArticlesByUserId(
        principal: Principal?,
        @PathVariable writerId: String
    ): Flux<Article> = articleService.getArticlesByUser(
        (principal as UsernamePasswordAuthenticationToken?)?.principal as User?,
        writerId
    )

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    fun getMyArticles(
        principal: Principal
    ): Flux<Article> = articleService.getArticlesByUser(
        (principal as UsernamePasswordAuthenticationToken).principal as User,
        (principal.principal as User).id,
    )

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{articleId}/like")
    fun likeArticle(
        principal: Principal,
        @PathVariable articleId: String,
        @RequestBody likeArticleRequest: LikeArticleRequest
    ): Mono<Boolean> = articleService.likeArticle(
        (principal as UsernamePasswordAuthenticationToken).principal as User,
        articleId,
        likeArticleRequest
    )
}