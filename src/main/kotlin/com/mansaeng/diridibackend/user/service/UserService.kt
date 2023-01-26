package com.mansaeng.diridibackend.user.service

import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.user.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(private val userRepository: UserRepository) {


//    @PostConstruct
//    fun postConstruct() {
//        // test 1234
//        // $2a$10$cCN/klyh5zqMKSXTAc3sYe7hPS/iHya07hFy6JpERlY20GDAS9aK6
//        userRepository.save(
//            User(
//                UUID.randomUUID().toString(),
//                "userB",
//                "\$2a\$10\$cCN/klyh5zqMKSXTAc3sYe7hPS/iHya07hFy6JpERlY20GDAS9aK6",
//                true,
//                listOf(Role.ROLE_USER)
//            )
//        ).subscribe { user -> println(user) }
//    }

    fun findById(id: String): Mono<User> = userRepository.findById(id)

    fun findByUsername(username: String): Mono<User> = userRepository.findByUsername(username)
}