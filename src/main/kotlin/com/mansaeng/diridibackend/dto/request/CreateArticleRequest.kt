package com.mansaeng.diridibackend.dto.request

data class CreateArticleRequest(
    val title: String,
    val description: String,
    val tags: List<String> = listOf()
)
