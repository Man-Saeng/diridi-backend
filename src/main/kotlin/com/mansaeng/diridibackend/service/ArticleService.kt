package com.mansaeng.diridibackend.service

import com.mansaeng.diridibackend.dto.request.CreateArticleRequest
import com.mansaeng.diridibackend.dto.request.LikeArticleRequest
import com.mansaeng.diridibackend.entity.article.Article
import com.mansaeng.diridibackend.entity.article.StatusType
import com.mansaeng.diridibackend.entity.user.User
import com.mansaeng.diridibackend.repository.ArticleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

    fun createArticle(writer: User, createArticleRequest: CreateArticleRequest): Mono<Article> {
        val (title, description, category, tags) = createArticleRequest
        val article =
            Article(title = title, description = description, writerId = writer.id, category = category, tags = tags)
        return articleRepository.save(article)
    }

    fun getArticleList(tag: String?, skip: Int, take: Int): Flux<Article> {
        val page = PageRequest.of(skip, take)

        return if (tag == null) articleRepository.findByStatusIsNot(
            StatusType.PREPARE,
            page
        ) else articleRepository.findByTagsContainingAndStatusIsNot(
            tag,
            StatusType.PREPARE,
            page
        )
    }

    fun getArticlesByUser(currentUser: User?, targetUserId: String): Flux<Article> {
        println(currentUser)

        val isWriter = currentUser?.id == targetUserId

        return if (isWriter) articleRepository.findByWriterId(targetUserId)
        else articleRepository.findByWriterIdAndStatus(
            targetUserId,
            StatusType.PUBLISH
        )
    }

    fun getArticleDetailById(articleId: String): Mono<Article> = articleRepository.findById(articleId)

    fun getLikedArticles(user: User): Flux<Article> = articleRepository.findByLikedUsersContains(user.id)

    fun likeArticle(user: User, articleId: String, likeArticleRequest: LikeArticleRequest): Mono<Boolean> {
        val (like) = likeArticleRequest

        return articleRepository.findById(articleId)
            .mapNotNull { it }
            .switchIfEmpty(Mono.error(RuntimeException("Article ID??? ??????????????????")))
            .flatMap {
                it as Article
                val likedAlready = it.likedUsers.indexOf(user.id) != -1
                if (like && !likedAlready) {
                    // ???????????? ??? ?????? ?????????, ???????????? ??? ??????
                    val mutableLikedUsers = it.likedUsers.toMutableList()
                    mutableLikedUsers.add(user.id)
                    it.likedUsers = mutableLikedUsers
                    return@flatMap articleRepository.save(it)
                } else if (!like && likedAlready) {
                    // ???????????? ??? ?????? ????????? ???????????? ???????????? ???
                    val mutableLikedUsers = it.likedUsers.toMutableList()
                    mutableLikedUsers.remove(user.id)
                    it.likedUsers = mutableLikedUsers
                    return@flatMap articleRepository.save(it)
                } else {
                    Mono.error(RuntimeException("?????? ???????????? ?????????, ???????????? ??????????????????."))
                }
            }
            .map { like }
    }
}