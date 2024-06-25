package com.jb.tdl2.domain.auth.client

import com.jb.tdl2.domain.auth.dto.GoogleUserInfoResponse
import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.domain.auth.dto.UserInfoResponse
import com.jb.tdl2.domain.auth.properties.google.GoogleConfig
import com.jb.tdl2.domain.exception.InvalidTokenException
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component(value = "googleOAuthClient")
class GoogleOAuthClient(
    private val googleConfig: GoogleConfig,
    private val restClient: RestClient

) : OAuthClient {
    override fun generateLoginPage(): String {
        return StringBuilder(googleConfig.authServerBaseUrl)
            .append("?client_id=").append(googleConfig.clientId)
            .append("&redirect_uri=").append(googleConfig.redirectUrl)
            .append("&scope=email profile")
            .append("&response_type=code")
            .toString()
    }

    override fun getAccessToken(code: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to googleConfig.clientId,
            "client_secret" to googleConfig.clientSecret,
            "redirect_uri" to googleConfig.redirectUrl,
            "code" to code,
        )
        return restClient.post()
            .uri(googleConfig.authServerTokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw InvalidTokenException("google")
            }
            .body<TokenResponse>()
            ?.accessToken
            ?: throw InvalidTokenException("google")

    }

    override fun retrieveUserInfo(token: String): GoogleUserInfoResponse {
        return restClient.get()
            .uri("${googleConfig.resourceServerBaseUrl}/oauth2/v1/userinfo")
            .header("Authorization", "Bearer $token")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw throw InvalidTokenException("google")
            }
            .body<GoogleUserInfoResponse>()
            ?: throw throw InvalidTokenException("google")
    }


}