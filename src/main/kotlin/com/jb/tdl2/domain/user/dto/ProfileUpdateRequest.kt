package com.jb.tdl2.domain.user.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ProfileUpdateRequest(

    @field:Size(min = 2, max = 12, message = "닉네임은 2글자 이상 12글자 이하여야 합니다")
    @field:Pattern(regexp = "^[가-힣a-zA-Z0-9]*\$", message = "닉네임은 한글, 영어 대소문자, 숫자만 포함 가능합니다")
    val nickname: String,

)
