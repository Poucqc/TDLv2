package com.jb.tdl2.domain.post.dto

import java.time.LocalDateTime

data class PostListResponse(
    val id: Long,
    val title: String,
    val hashtag: List<String>,
    val createdAt: LocalDateTime,
    val authorName: String,
    val likesCount: Int,
)