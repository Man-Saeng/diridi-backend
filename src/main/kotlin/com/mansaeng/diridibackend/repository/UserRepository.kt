package com.mansaeng.diridibackend.repository

import com.mansaeng.diridibackend.entity.user.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsername(username: String): Mono<User>
}