package com.jb.tdl2.domain.post.dto

import java.time.LocalDateTime

data class CursorPageResponse (
    val postList: List<PostListResponse>,
    val nextCursor: LocalDateTime?
)

