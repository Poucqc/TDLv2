package com.jb.tdl2.domain.auth.controller

import com.jb.tdl2.domain.auth.dto.TokenResponse
import com.jb.tdl2.domain.auth.service.OAuthService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/oauth")
class OAuthController(
    private val oAuthService: OAuthService,
) {

    @GetMapping("/login/{provider}")
    fun redirectLoginPage(
        @PathVariable provider: String,
        response: HttpServletResponse
    ) {
        val url = oAuthService.getLoginPage(provider)
        response.sendRedirect(url)
    }

    @GetMapping("/login/{provider}/callback")
    fun callback(
        @PathVariable provider: String,
        @RequestParam code: String,
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(oAuthService.loginCallback(provider, code))
    }

}