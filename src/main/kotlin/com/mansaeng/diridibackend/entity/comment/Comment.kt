package com.mansaeng.diridibackend.entity.comment

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Comment(
    @Id val id: String? = null,
    val writerId: String,
    val articleId: String,
    val parentId: String?,
    val content: String,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)