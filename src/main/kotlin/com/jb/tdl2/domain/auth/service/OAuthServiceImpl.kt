package com.jb.tdl2.domain.auth.service

import com.jb.tdl2.domain.auth.client.OAuthClient
import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.domain.user.service.UserService
import com.jb.tdl2.security.jwt.JwtPlugin
import java.security.InvalidParameterException

class OAuthServiceImpl(
    private val oauthClient: Map<String, OAuthClient>,
    private val userService: UserService,
    private val jwtPlugin: JwtPlugin
) : OAuthService {
    override fun getLoginPage(provider: String): String {
        val providerName = provider.lowercase()
        return oauthClient["${providerName}OAuthClient"]?.generateLoginPage()
            ?: throw InvalidParameterException("invalid provider")
    }

    override fun loginCallback(provider: String, code: String): TokenResponse {
        val providerName = provider.lowercase()
        return oauthClient["${providerName}OAuthClient"]?.getAccessToken(code)
            ?.let { oauthClient["${providerName}OAuthClient"]?.retrieveUserInfo(it) }
            ?.let { userService.registerUserIfAbsent(it, providerName) }
            ?.let { jwtPlugin.generateAccessToken(it.id, "User") }
            ?.let { TokenResponse(it)}
            ?: throw RuntimeException("$providerName login fail")
    }

    // 좀 더 읽기 쉽게 외부 함수로 provider 구분
//    //upcasting
//    private fun defineProvider(provider: String): OAuthClient {
//        return when (provider) {
//            "naver" -> NaverOAuthClient()
//            "kakao" -> KakaoOAuthClient()
//            "google" -> GoogleOAuthClient()
//            else -> throw InvalidParameterException("Invalid provider")
//        }
//    }

}

