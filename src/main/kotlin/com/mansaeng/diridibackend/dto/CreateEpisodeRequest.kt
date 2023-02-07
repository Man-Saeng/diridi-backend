package com.mansaeng.diridibackend.dto

import java.time.LocalDateTime

data class CreateEpisodeRequest(
    val title: String,
    val content: String,
    val publishedAt: LocalDateTime,
    val articleId: String
)