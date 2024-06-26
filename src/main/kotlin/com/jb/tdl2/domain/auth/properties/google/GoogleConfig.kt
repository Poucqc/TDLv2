package com.jb.tdl2.domain.auth.properties.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "oauth2.google")
data class GoogleConfig(
    val clientId: String = "",
    val clientSecret: String = "",
    val redirectUrl: String = "",
    val authServerBaseUrl: String = "",
    val authServerTokenUrl: String = "",
    val resourceServerBaseUrl: String = "",
)