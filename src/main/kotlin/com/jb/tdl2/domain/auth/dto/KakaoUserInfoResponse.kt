package com.jb.tdl2.domain.auth.dto

data class KakaoUserInfoResponse(
    override val id: String,
    override val email: String,
    override val nickname: String,
) : UserInfoResponse
