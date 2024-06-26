package com.jb.tdl2.domain.post.dto

data class UpdatePostRequest(
    val title: String,
    val content: String,
    val hashtag: Set<String>
)
