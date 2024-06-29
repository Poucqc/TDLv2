package com.jb.tdl2.domain.comment.dto

import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val body: String,
    val updatedAt: LocalDateTime,
)
