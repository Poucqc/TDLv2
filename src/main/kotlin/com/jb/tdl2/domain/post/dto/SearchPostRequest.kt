package com.jb.tdl2.domain.post.dto

import com.jb.tdl2.domain.post.model.Hashtag
import java.time.LocalDateTime

data class SearchPostRequest(
    val keyword: String,
    val hashtags: Set<Hashtag>,
    val likesCount: Int,
    val createdAfter: LocalDateTime
)


