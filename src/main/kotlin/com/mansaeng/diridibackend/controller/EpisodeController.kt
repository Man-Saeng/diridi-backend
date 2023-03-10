package com.mansaeng.diridibackend.controller

import com.mansaeng.diridibackend.dto.request.CreateEpisodeRequest
import com.mansaeng.diridibackend.dto.request.LikeEpisodeRequest
import com.mansaeng.diridibackend.dto.request.ModifyEpisodeRequest
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

    @GetMapping
    fun getEpisodeListByArticleId(
        @RequestParam("articleId") articleId: String,
        @RequestParam("page") skip: Int?, @RequestParam("take") take: Int?
    ): ResponseEntity<Flux<Episode>> =
        ResponseEntity.ok(episodeService.getEpisodeListByArticleId(articleId, skip ?: 0, take ?: 10))

    @GetMapping("/{episodeId}")
    fun getEpisodeDetail(
        @PathVariable("episodeId") episodeId: String
    ): ResponseEntity<Mono<Episode>> = ResponseEntity.ok(episodeService.getEpisodeDetail(episodeId))

    @PatchMapping("/{episodeId}")
    fun modifyEpisode(
        principal: Principal,
        @PathVariable("episodeId") episodeId: String,
        @RequestBody modifyEpisodeRequest: ModifyEpisodeRequest
    ): ResponseEntity<Mono<String>> = ResponseEntity.ok(
        episodeService.modifyEpisode(
            (principal as UsernamePasswordAuthenticationToken).principal as User,
            modifyEpisodeRequest
        ).mapNotNull { episode -> episode.id }
    )

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{episodeId}/like")
    fun likeEpisode(
        principal: Principal,
        @PathVariable episodeId: String,
        @RequestBody likeEpisodeRequest: LikeEpisodeRequest
    ): Mono<Boolean> = episodeService.likeEpisode(
        (principal as UsernamePasswordAuthenticationToken).principal as User,
        episodeId,
        likeEpisodeRequest
    )
}