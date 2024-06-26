package com.jb.tdl2.domain.post.dto

import com.jb.tdl2.domain.comment.model.Comment
import com.jb.tdl2.domain.post.model.Hashtag
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val updatedAt: LocalDateTime,
    val hashtag: Hashtag,
    val comments: List<Comment>,
    val authorId: Long,
    val authorName: String,
    val likesCount: Int,
)
