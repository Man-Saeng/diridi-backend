package com.mansaeng.diridibackend.entity.article

import com.fasterxml.jackson.annotation.JsonProperty

enum class StatusType(@JsonProperty val value: String) {
    PREPARE("prepare"),
    PUBLISH("publish"),
    REST("rest"),
    COMPLETE("complete")
}