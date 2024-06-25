package com.jb.tdl2.domain.auth.properties.google

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GoogleConfig::class)
class GoogleProperties {
    @Bean
    fun googleConfig(): GoogleConfig {
        return GoogleConfig()
    }
}