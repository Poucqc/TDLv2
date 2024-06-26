package com.jb.tdl2.domain.user.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class PasswordChangeRequest(

    val currentPassword: String,

    @field:Size(min = 8, max = 16, message = "비밀번호는 8글자 이상 16글자 이하여야 합니다")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*[!@#\$%^&*()-+]).{8,16}\$",
        message = "비밀번호는 영어 대소문자를 포함하고, 최소 하나의 특수문자(!@#\$%^&*()-+)를 포함해야 합니다"
    )
    val newPassword: String,

    val newPasswordConfirmation: String,
)
