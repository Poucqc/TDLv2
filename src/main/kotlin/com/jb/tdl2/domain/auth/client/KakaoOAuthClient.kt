package com.jb.tdl2.domain.auth.client

import com.jb.tdl2.domain.auth.dto.KakaoUserInfoResponse
import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.domain.auth.dto.UserInfoResponse
import org.springframework.stereotype.Component

@Component(value = "kakaoOAuthClient")
class KakaoOAuthClient:OAuthClient{
    override fun generateLoginPage(): String {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(code: String): String {
        TODO("Not yet implemented")
    }

    override fun retrieveUserInfo(token: String): KakaoUserInfoResponse {
        TODO("Not yet implemented")
    }

}