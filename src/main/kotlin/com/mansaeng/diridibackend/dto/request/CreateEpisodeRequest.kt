package com.mansaeng.diridibackend.dto.request

import java.time.LocalDateTime

data class CreateEpisodeRequest(
    val title: String,
    val content: String,
    val publishedAt: LocalDateTime,
    val articleId: String
)