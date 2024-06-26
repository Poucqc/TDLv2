package com.jb.tdl2.domain.auth.properties.naver

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(NaverConfig::class)
class NaverProperties {
    @Bean
    fun naverConfig(): NaverConfig {
        return NaverConfig()
    }
}