package com.mansaeng.diridibackend.entity.article

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Episode(
    @Id
    val id: String? = null,

    var title: String,
    var content: String,
    var publishedAt: LocalDateTime,
    var writerId: String,
    var articleId: String,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
