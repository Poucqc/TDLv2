package com.jb.tdl2.domain.post.dto

import java.time.LocalDateTime

data class PostListResponse(
    val id: Long,
    val title: String,
    val hashtag: Set<String>,
    val updatedAt: LocalDateTime,
    val authorName: String,
    val likesCount: Int,
)