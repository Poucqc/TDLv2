package com.jb.tdl2.domain.auth.properties.kakao

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KakaoConfig::class)
class KakaoProperties {
    @Bean
    fun kakaoConfig(): KakaoConfig {
        return KakaoConfig()
    }
}