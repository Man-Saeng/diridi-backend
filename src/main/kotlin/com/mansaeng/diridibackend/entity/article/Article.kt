package com.mansaeng.diridibackend.entity.article

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Article(
    @Id
    val id: String? = null,
    var title: String,
    var description: String,
    var thumbnailImageLink: String = "https://via.placeholder.com/110x165?text=Diridi",
    var writerId: String,
    var category: CategoryType,
    var status: StatusType = StatusType.PREPARE,
    var episodeIds: List<String> = mutableListOf(),
    var tags: List<String> = mutableListOf(),

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)