package com.jb.tdl2.domain.post.dto

import jakarta.validation.constraints.Pattern

data class CursorRequest(
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{7}$", message = "'2024-06-29T00:00:00.0000000' 형식으로 입력해 주세요")
    val cursorTime: String,

    val cursorLikeCount: Int,

    val orderBy: String
)
