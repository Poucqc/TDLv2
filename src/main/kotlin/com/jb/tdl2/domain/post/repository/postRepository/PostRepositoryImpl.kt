package com.jb.tdl2.domain.post.repository.postRepository

import com.jb.tdl2.domain.post.dto.CursorRequest
import com.jb.tdl2.domain.post.dto.PostListResponse
import com.jb.tdl2.domain.post.dto.SearchPostRequest
import com.jb.tdl2.domain.post.model.*
import com.jb.tdl2.domain.user.model.QUser
import com.jb.tdl2.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class PostRepositoryImpl : CustomPostRepository, QueryDslSupport() {
    private val post = QPost.post
    private val user = QUser.user
    private val like = QLike.like
    private val hashtag = QHashtag.hashtag
    private val postHashtag = QPostHashtag.postHashtag

    override fun getPostList(pageable: Pageable, cursor: CursorRequest): List<PostListResponse> {
        val query = queryFactory.select(
            Projections.constructor(
                PostListResponse::class.java,
                post.id,
                post.title,
                Expressions.list(hashtag.name).`as`("hashtags"),
                post.createdAt,
                post.user.nickname,
                like.countDistinct()
            )
        ).from(post)
            .leftJoin(postHashtag).on(post.eq(postHashtag.post))
            .leftJoin(hashtag).on(postHashtag.hashtag.eq(hashtag))
            .leftJoin(user).on(post.user.eq(user))
            .leftJoin(like).on(post.id.eq(like.id.postId))
            .groupBy(
                post.id,
                post.title,
                post.createdAt,
                post.user.nickname,
            )
            .having(applyCursorPosition(cursor))
            .applyOrderBy(cursor.orderBy)
            .orderBy(post.id.desc())
            .limit(pageable.pageSize.toLong())
            .fetch()
        return query
    }

    override fun searchPosts(request: SearchPostRequest, pageable: Pageable, orderBy: String): List<PostListResponse> {
        val query = queryFactory.select(
            Projections.constructor(
                PostListResponse::class.java,
                post.id,
                post.title,
                Expressions.list(hashtag.name).`as`("hashtags"),
                post.createdAt,
                post.user.nickname,
                like.countDistinct()
            )
        ).from(post)
            .leftJoin(postHashtag).on(post.eq(postHashtag.post))
            .leftJoin(hashtag).on(postHashtag.hashtag.eq(hashtag))
            .leftJoin(user).on(post.user.eq(user))
            .leftJoin(like).on(post.id.eq(like.id.postId))
            .groupBy(
                post.id,
                post.title,
                Expressions.list(hashtag.name).`as`("hashtags"),
                post.createdAt,
                post.user.nickname,
            )
            .having(searchByRequest(request))
            .orderBy(post.createdAt.desc())
            .orderBy(post.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return query
    }

    override fun findHashtagsByPostId(postId: Long): Set<Hashtag> {
        return queryFactory.select(hashtag)
            .from(postHashtag)
            .innerJoin(hashtag).on(postHashtag.hashtag.eq(hashtag))
            .where(postHashtag.post.id.eq(postId))
            .fetch().toSet()
    }

    private fun applyCursorPosition(cursor: CursorRequest): BooleanBuilder {
        val builder = BooleanBuilder()
        if (cursor.orderBy == "createdAt") {
            cursor.cursorTime.let {
                val determineCursor = LocalDateTime.parse(cursor.cursorTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                builder.and(post.createdAt.lt(determineCursor))
            }
        } else if (cursor.orderBy == "likeCount") {
            cursor.cursorLikeCount.let {
                builder.and(like.count().lt(it))
            }
        } else {
            throw IllegalArgumentException("cursor orderBy must 'createdAt' or 'likeCount'")
        }
        return builder
    }

    private fun <T> JPAQuery<T>.applyOrderBy(orderBy: String): JPAQuery<T> {
        return when (orderBy) {
            "createdAt" -> this.orderBy(post.createdAt.desc())
            "likeCount" -> this.orderBy(like.count().desc())
            else -> throw IllegalArgumentException("orderBy must 'createdAt' or 'likeCount'")
        }
    }

    private fun searchByRequest(request: SearchPostRequest): BooleanBuilder {
        val builder = BooleanBuilder()

        request.keyword?.let { keyword ->
            val keywordExpression = "%$keyword%"
            builder.and(
                post.title.like(keywordExpression)
                    .or(post.content.like(keywordExpression))
                    .or(
                        post.`in`(
                            JPAExpressions.select(postHashtag.post)
                                .from(postHashtag)
                                .innerJoin(postHashtag.hashtag)
                                .where(postHashtag.hashtag.name.like(keywordExpression))
                        )
                    )
            )
        }

        request.createdAfter?.let { builder.and(post.createdAt.after(it)) }
        request.likesCount?.let { builder.and(like.count().goe(it)) }

        return builder
    }

}