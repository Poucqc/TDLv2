package com.jb.tdl2.domain.post.dto

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.post.model.Hashtag
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val updatedAt: LocalDateTime,
    val hashtag: MutableSet<Hashtag>,
    val comments: List<CommentResponse>,
    val authorId: Long,
    val authorName: String,
    val likesCount: Int,
    val viewCount: Int,
)
