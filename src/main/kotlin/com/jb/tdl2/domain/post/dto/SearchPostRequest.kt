package com.jb.tdl2.domain.post.dto

import java.time.LocalDateTime

data class SearchPostRequest(
    val keyword: String?,
    val likesCount: Int?,
    val createdAfter: LocalDateTime?,
)


