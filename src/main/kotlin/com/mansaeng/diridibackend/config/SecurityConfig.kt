package com.mansaeng.diridibackend.config

import com.mansaeng.diridibackend.config.security.ReactiveAuthenticationManagerImpl
import com.mansaeng.diridibackend.config.security.SecurityContextRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(
    private val authenticationManagerImpl: ReactiveAuthenticationManagerImpl,
    private val securityContextRepositoryImpl: SecurityContextRepositoryImpl
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .exceptionHandling()
        .authenticationEntryPoint { swe, e ->
            Mono.fromRunnable { -> swe.response.statusCode = HttpStatus.UNAUTHORIZED }
        }
        .accessDeniedHandler { swe, e -> Mono.fromRunnable { -> swe.response.statusCode = HttpStatus.FORBIDDEN } }
        .and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .authenticationManager(authenticationManagerImpl)
        .securityContextRepository(securityContextRepositoryImpl)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/login").permitAll()
        .pathMatchers("/article/**").permitAll()
        .pathMatchers("/episode/**").permitAll()
        .anyExchange().authenticated()
        .and().build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}