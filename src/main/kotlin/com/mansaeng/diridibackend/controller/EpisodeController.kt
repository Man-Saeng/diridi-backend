package com.mansaeng.diridibackend.controller

import com.mansaeng.diridibackend.dto.CreateEpisodeRequest
import com.mansaeng.diridibackend.entity.article.Episode
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.service.EpisodeService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/episode")
class EpisodeController(private val episodeService: EpisodeService) {

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    fun createEpisode(
        principal: Principal,
        @RequestBody createEpisodeRequest: CreateEpisodeRequest
    ): Mono<ResponseEntity<String>> = episodeService.createEpisode(
        (principal as UsernamePasswordAuthenticationToken).principal as User,
        createEpisodeRequest
    ).mapNotNull { episode -> ResponseEntity.ok(episode.id) }

    @GetMapping("/{articleId}")
    fun getEpisodeListByArticleId(
        @PathVariable articleId: String,
        @RequestParam("page") skip: Int = 0, @RequestParam("take") take: Int = 10
    ): ResponseEntity<Flux<Episode>> =
        ResponseEntity.ok(episodeService.getEpisodeListByArticleId(articleId, skip, take))
}