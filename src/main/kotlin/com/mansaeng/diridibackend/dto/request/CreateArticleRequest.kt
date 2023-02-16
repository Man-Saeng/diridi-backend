package com.mansaeng.diridibackend.dto.request

import com.mansaeng.diridibackend.entity.article.CategoryType

data class CreateArticleRequest(
    val title: String,
    val description: String,
    val category: CategoryType,
    val tags: List<String> = listOf()
)
