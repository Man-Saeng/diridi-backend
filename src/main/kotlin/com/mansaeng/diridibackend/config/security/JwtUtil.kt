package com.mansaeng.diridibackend.config.security

import com.mansaeng.diridibackend.entity.user.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct

@Component
class JwtUtil(
    @Value("\${jwt.access-secret}") private val accessSecret: String,
    @Value("\${jwt.access-expiration}") private val accessExpiration: Long
) {
    private lateinit var accessKey: Key

    @PostConstruct
    fun init() {
        accessKey = Keys.hmacShaKeyFor(accessSecret.toByteArray(StandardCharsets.UTF_8))
    }

    fun getAllClaimsFromAccessToken(accessToken: String): Claims =
        Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken).body

    fun getUserIdFromAccessToken(accessToken: String): String = getAllClaimsFromAccessToken(accessToken).subject

    fun getExpirationFromAccessToken(accessToken: String): Date = getAllClaimsFromAccessToken(accessToken).expiration

    private fun isTokenExpired(accessToken: String): Boolean = getExpirationFromAccessToken(accessToken).before(Date())

    fun createAccessToken(user: User): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(user.id)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + accessExpiration))
            .signWith(accessKey)
            .compact()
    }

    fun validateAccessToken(accessToken: String): Boolean = !isTokenExpired(accessToken)
}