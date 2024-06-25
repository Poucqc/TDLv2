package com.jb.tdl2.domain.auth.service

import com.jb.tdl2.domain.auth.client.GoogleOAuthClient
import com.jb.tdl2.domain.auth.client.KakaoOAuthClient
import com.jb.tdl2.domain.auth.client.NaverOAuthClient
import com.jb.tdl2.domain.auth.client.OAuthClient
import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.security.jwt.JwtPlugin
import java.security.InvalidParameterException

class OAuthServiceImpl(
    private val oauthClient: Map<String, OAuthClient>,
    private val userService: UserService,
    private val jwtPlugin: JwtPlugin
) : OAuthService {
    override fun getLoginPage(provider: String): String {
        val providerToLowerCase = provider.lowercase()
        return oauthClient["${providerToLowerCase}OAuthClient"]?.generateLoginPage()
            ?: throw InvalidParameterException("invalid provider")
    }

    override fun loginCallback(provider: String, code: String): TokenResponse {
        val providerToLowerCase = provider.lowercase()
        return oauthClient["${providerToLowerCase}OAuthClient"]?.getAccessToken(code)
            ?.let { oauthClient["${providerToLowerCase}OAuthClient"]?.retrieveUserInfo(it) }
            .let { userService.registerUserIfAbsent(it, providerToLowerCase) }
            .let { jwtPlugin.generateAccessToken(it.id!!, it.email!!, "User") }
            ?: throw RuntimeException("$providerToLowerCase login fail")
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

