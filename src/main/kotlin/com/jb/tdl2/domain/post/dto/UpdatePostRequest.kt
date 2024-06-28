package com.jb.tdl2.domain.post.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdatePostRequest(

    @field:Size(min = 1, max = 50, message = "제목을 1~50 글자 이내로 입력해 주세요")
    val title: String,
    @field:Size(min = 1, max = 2000, message = "본문을 1~2000 글자 이내로 입력해 주세요")
    val content: String,
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]*$", message = "영어, 한글, 숫자만 포함되어야 합니다")
    @field:Size(min =1, max = 12, message = "해쉬태그는 1~12 글자 이내여야 합니다")
    val hashtag: Set<String>
)
