package com.mansaeng.diridibackend.entity.article

import com.fasterxml.jackson.annotation.JsonValue

enum class CategoryType(@JsonValue val code: String) {
    FANTASY("fantasy"),
    BL("bl"),
    ROMANCE_FANTASY("romance_fantasy"),
    ROMANCE("romance"),
    FREE("free")
}
