package com.mansaeng.diridibackend.repository

import com.mansaeng.diridibackend.entity.article.Article
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ArticleRepository : ReactiveMongoRepository<Article, String> {
}