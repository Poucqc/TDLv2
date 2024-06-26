package com.jb.tdl2.domain.auth.client

import com.jb.tdl2.domain.auth.dto.UserInfoResponse

interface OAuthClient {

    fun generateLoginPage(): String

    fun getAccessToken(code: String): String

    fun retrieveUserInfo(token: String): UserInfoResponse
}