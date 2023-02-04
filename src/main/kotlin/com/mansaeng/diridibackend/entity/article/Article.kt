package com.mansaeng.diridibackend.entity.article

import com.mansaeng.diridibackend.entity.user.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDateTime

@Document
data class Article(
    @Id
    val id: String? = null,
    var title: String,
    var description: String,

    @DocumentReference
    var writer: User,

    @DocumentReference(lazy = true)
    var episodes: List<Episode> = mutableListOf(),

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)