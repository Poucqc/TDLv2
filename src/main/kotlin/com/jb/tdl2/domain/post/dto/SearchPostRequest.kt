package com.jb.tdl2.domain.post.dto

import com.jb.tdl2.domain.post.model.Hashtag

data class SearchPostRequest(
    val keyword: String,
    val excludeKeyword: String,
    val includeHashtags: Set<Hashtag>,
    val excludeHashtags: Set<Hashtag>
)

