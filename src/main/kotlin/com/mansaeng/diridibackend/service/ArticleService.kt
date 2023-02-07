package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.CreateArticleRequest
import com.mansaeng.diridibackend.entity.article.Article
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.repository.ArticleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

    fun createArticle(writer: User, createArticleRequest: CreateArticleRequest): Mono<Article> {
        val (title, description) = createArticleRequest
        val article = Article(title = title, description = description, writerId = writer.id)
        return articleRepository.save(article)
    }

    fun getArticleList(skip: Int, take: Int): Flux<Article> {
        val page = PageRequest.of(skip, take)

        return articleRepository.findAllPaged(page)
    }

    fun getArticleDetailById(articleId: String): Mono<Article> = articleRepository.findById(articleId)
}