package com.jb.tdl2.domain.auth.client

import com.jb.tdl2.domain.auth.dto.GoogleUserInfoResponse
import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.domain.auth.dto.UserInfoResponse
import org.springframework.stereotype.Component

@Component(value = "googleOAuthClient")
class GoogleOAuthClient : OAuthClient {
    override fun generateLoginPage(): String {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(code: String): String {
        TODO("Not yet implemented")
    }

    override fun retrieveUserInfo(token: String): GoogleUserInfoResponse {
        TODO("Not yet implemented")
    }


}