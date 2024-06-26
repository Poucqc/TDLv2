package com.jb.tdl2.domain.auth.properties.naver

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "oauth2.naver")
data class NaverConfig (
    val clientId: String = "",
    val clientSecret: String = "",
    val redirectUrl: String = "",
    val authServerBaseUrl: String = "",
    val resourceServerBaseUrl: String = "",
)