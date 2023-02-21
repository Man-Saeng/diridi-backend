package com.mansaeng.diridibackend.dto.request

data class CreateCommentRequest(val articleId: String, val parentId: String?, val content: String)