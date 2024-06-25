package com.jb.tdl2.domain.auth.dto

data class GoogleUserInfoResponse(
    override val id: Long,
    override val email: String,
    override val nickname: String,
) : UserInfoResponse
