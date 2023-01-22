package com.mansaeng.diridibackend.config.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class SecurityContextRepositoryImpl(private val reactiveAuthenticationManagerImpl: ReactiveAuthenticationManagerImpl) :
    ServerSecurityContextRepository {
    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet")
    }

    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
        return Mono.justOrEmpty(exchange?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { authHeader -> authHeader.startsWith("Bearer ") }
            .flatMap { authHeader ->
                val authToken: String = authHeader.substring(7)
                val auth: Authentication = UsernamePasswordAuthenticationToken(authToken, authToken)
                this.reactiveAuthenticationManagerImpl.authenticate(auth).map { SecurityContextImpl() }
            }
    }
}