package com.jb.tdl2.domain.auth.client

import com.jb.tdl2.domain.auth.dto.NaverUserInfoResponse
import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.domain.auth.properties.naver.NaverConfig
import com.jb.tdl2.domain.exception.InvalidTokenException
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component(value = "naverOAuthClient")
class NaverOAuthClient(
    private val naverConfig: NaverConfig,
    private val restClient: RestClient,
) : OAuthClient {
    override fun generateLoginPage(): String {
        return StringBuilder(naverConfig.authServerBaseUrl)
            .append("/oauth2.0/authorize")
            .append("?client_id=").append(naverConfig.clientId)
            .append("&redirect_uri=").append(naverConfig.redirectUrl)
            .append("&response_type=").append("code")
            .toString()
    }

    override fun getAccessToken(code: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to naverConfig.clientId,
            "client_secret" to naverConfig.clientSecret,
            "redirect_uri" to naverConfig.redirectUrl,
            "code" to code,
        )

        return restClient.post()
            .uri("${naverConfig.authServerBaseUrl}/oauth2.0/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw InvalidTokenException("naver")
            }
            .body<TokenResponse>()
            ?.accessToken
            ?: throw InvalidTokenException("naver")

    }

    override fun retrieveUserInfo(token: String): NaverUserInfoResponse {
        return restClient.get()
            .uri("${naverConfig.resourceServerBaseUrl}/v1/nid/me")
            .header("Authorization", "Bearer $token")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw InvalidTokenException("naver")
            }
            .body<NaverUserInfoResponse>()
            ?: throw InvalidTokenException("naver")
    }


}