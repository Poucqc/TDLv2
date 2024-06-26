package com.jb.tdl2.domain.auth.dto

data class NaverUserInfoResponse(
    override val id: String,
    override val email: String,
    override val nickname: String,
) : UserInfoResponse {
}
