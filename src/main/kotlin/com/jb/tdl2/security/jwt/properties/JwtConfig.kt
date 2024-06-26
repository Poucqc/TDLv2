package com.jb.tdl2.security.jwt.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "auth.jwt")
data class JwtConfig(
    val issuer: String = "",
    val secret: String = "",
    val accessExpiration: Long = 0,
    val refreshExpiration: Long = 0,
)