package com.mansaeng.diridibackend.user.service

import com.mansaeng.diridibackend.entity.user.Role
import com.mansaeng.diridibackend.entity.user.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import javax.annotation.PostConstruct

@Service
class UserService {
    private var map: MutableMap<Long, User> = HashMap();

    @PostConstruct
    fun postConstruct() {
        // test 1234
        // $2a$10$cCN/klyh5zqMKSXTAc3sYe7hPS/iHya07hFy6JpERlY20GDAS9aK6
        map.put(
            1,
            User(
                1,
                "admin",
                "\$2a\$10\$cCN/klyh5zqMKSXTAc3sYe7hPS/iHya07hFy6JpERlY20GDAS9aK6",
                true,
                listOf(Role.ROLE_ADMIN)
            )
        )
        map.put(
            2,
            User(
                2,
                "userA",
                "\$2a\$10\$cCN/klyh5zqMKSXTAc3sYe7hPS/iHya07hFy6JpERlY20GDAS9aK6",
                true,
                listOf(Role.ROLE_USER)
            )
        )
        map.put(
            3,
            User(
                3,
                "userB",
                "\$2a\$10\$cCN/klyh5zqMKSXTAc3sYe7hPS/iHya07hFy6JpERlY20GDAS9aK6",
                true,
                listOf(Role.ROLE_USER)
            )
        )
    }

    fun findById(id: Long): Mono<User> = map[id].toMono()

    fun findByUsername(username: String): Mono<User> = map.values.find { user -> user.username == username }.toMono()
}