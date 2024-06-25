package com.jb.tdl2.domain.auth.service

import com.jb.tdl2.domain.auth.dto.TokenResponse
import org.springframework.stereotype.Service
import java.security.AuthProvider

@Service
interface OAuthService {
    fun getLoginPage(provider: String): String

    fun loginCallback(provider: String, code: String): TokenResponse
}