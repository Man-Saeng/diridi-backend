package com.mansaeng.diridibackend.repository

import com.mansaeng.diridibackend.entity.article.Episode
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface EpisodeRepository : ReactiveMongoRepository<Episode, String> {
}