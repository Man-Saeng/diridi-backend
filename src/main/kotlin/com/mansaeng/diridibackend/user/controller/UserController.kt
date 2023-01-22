package com.mansaeng.diridibackend.user.controller

import com.mansaeng.diridibackend.config.security.JwtUtil
import com.mansaeng.diridibackend.user.dto.AuthRequest
import com.mansaeng.diridibackend.user.dto.AuthResponse
import com.mansaeng.diridibackend.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class UserController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: AuthRequest): Mono<ResponseEntity<AuthResponse>> {
        return userService.findByUsername(loginRequest.username)
            .filter { user -> passwordEncoder.matches(loginRequest.password, user.password) }
            .map { user -> ResponseEntity.ok(AuthResponse(jwtUtil.createAccessToken(user))) }
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
    }
}