package com.jb.tdl2.security.jwt.properties

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JwtConfig::class)
class JwtProperties {
    @Bean
    fun jwtConfig(): JwtConfig {
        return JwtConfig()
    }
}