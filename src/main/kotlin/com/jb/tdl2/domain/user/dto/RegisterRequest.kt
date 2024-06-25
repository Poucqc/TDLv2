package com.jb.tdl2.domain.user.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val passwordConfirmation: String,
    val nickname: String,
)
