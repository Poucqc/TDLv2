package com.jb.tdl2.domain.auth.client

import com.jb.tdl2.domain.auth.dto.KakaoUserInfoResponse
import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.domain.auth.dto.UserInfoResponse
import com.jb.tdl2.domain.auth.properties.kakao.KakaoConfig
import com.jb.tdl2.domain.exception.InvalidTokenException
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component(value = "kakaoOAuthClient")
class KakaoOAuthClient(
    private val kakaoConfig: KakaoConfig,
    private val restClient: RestClient
) : OAuthClient {
    override fun generateLoginPage(): String {
        return StringBuilder(kakaoConfig.authServerBaseUrl)
            .append("/oauth/authorize")
            .append("?client_id=").append(kakaoConfig.clientId)
            .append("&redirect_uri=").append(kakaoConfig.redirectUrl)
            .append("&response_type=").append("code")
            .toString()
    }

    override fun getAccessToken(code: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to kakaoConfig.clientId,
            "code" to code
        )
        return restClient.post()
            .uri("${kakaoConfig.authServerBaseUrl}/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw InvalidTokenException("kakao")
            }
            .body<TokenResponse>()
            ?.accessToken
            ?: throw InvalidTokenException("kakao")
    }

    override fun retrieveUserInfo(token: String): KakaoUserInfoResponse {
        return restClient.get()
            .uri("${kakaoConfig.authServerBaseUrl}/v2/user/me")
            .header("Authorization", "Bearer $token")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw InvalidTokenException("kakao")
            }
            .body<KakaoUserInfoResponse>()
            ?: throw InvalidTokenException("kakao")
    }

}