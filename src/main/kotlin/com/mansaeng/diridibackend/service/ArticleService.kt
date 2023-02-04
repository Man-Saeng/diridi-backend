package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.CreateArticleRequest
import com.mansaeng.diridibackend.entity.article.Article
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.repository.ArticleRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

    fun createArticle(writer: User, createArticleRequest: CreateArticleRequest): Mono<Article> {
        val (title, description) = createArticleRequest
        val article = Article(title = title, description = description, writer = writer)
        return articleRepository.save(article)
    }
}