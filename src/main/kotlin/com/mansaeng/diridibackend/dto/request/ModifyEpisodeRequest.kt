package com.mansaeng.diridibackend.dto.request

import java.time.LocalDateTime

data class ModifyEpisodeRequest(
    val episodeId: String,
    val title: String?,
    val content: String?,
    val publishedAt: LocalDateTime?,
)
