package com.jb.tdl2.domain.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(

    @field:Email(message = "유효한 이메일 주소를 입력해주세요")
    val email: String,

    @field:Size(min = 8, max = 16, message = "비밀번호는 8글자 이상 16글자 이하여야 합니다")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*[!@#\$%^&*()-+]).{8,16}\$",
        message = "비밀번호는 영어 대소문자를 포함하고, 최소 하나의 특수문자(!@#\$%^&*()-+)를 포함해야 합니다"
    )
    val password: String,

    val passwordConfirmation: String,

    @field:Size(min = 2, max = 12, message = "닉네임은 2글자 이상 12글자 이하여야 합니다")
    @field:Pattern(regexp = "^[가-힣a-zA-Z0-9]*\$", message = "닉네임은 한글, 영어 대소문자, 숫자만 포함 가능합니다")
    val nickname: String,
)
