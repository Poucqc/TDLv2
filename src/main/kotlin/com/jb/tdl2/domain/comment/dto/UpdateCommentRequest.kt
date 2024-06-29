package com.jb.tdl2.domain.comment.dto

import jakarta.validation.constraints.Size

data class UpdateCommentRequest(
    @field:Size(min = 1, max = 200, message = "댓글 내용을 1자 이상 200자 이내로 적어주세요")
    val body: String,
)

