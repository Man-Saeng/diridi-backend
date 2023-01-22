package com.mansaeng.diridibackend.config.security

import com.mansaeng.diridibackend.user.service.UserService
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReactiveAuthenticationManagerImpl(private val jwtUtil: JwtUtil, private val userService: UserService) :
    ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val accessToken = authentication?.credentials.toString()
        val userId = jwtUtil.getUserIdFromAccessToken(accessToken)
        return Mono.just(jwtUtil.validateAccessToken(accessToken))
            .filter { valid -> valid }
            .switchIfEmpty(Mono.empty())
            .flatMap { valid ->
                userService.findById(userId)
                    .map { user ->
                        UsernamePasswordAuthenticationToken(
                            user.id,
                            null,
                            user.roles.map { role -> SimpleGrantedAuthority(role.name) })
                    }
            }
    }
}