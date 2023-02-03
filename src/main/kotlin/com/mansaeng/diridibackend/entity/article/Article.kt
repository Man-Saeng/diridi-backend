package com.mansaeng.diridibackend.entity.article

import com.mansaeng.diridibackend.entity.user.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document
data class Article(
    @Id
    val id: String? = null,
    var title: String,
    var description: String,

    @DocumentReference
    var writer: User,

    @DocumentReference(lazy = true)
    var episodes: List<Episode> = mutableListOf()
)