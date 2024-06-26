package com.jb.tdl2.security.jwt

import com.jb.tdl2.security.jwt.properties.JwtConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtPlugin(
    private val jwtConfig: JwtConfig
) {

    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    fun generateAccessToken(id: Long, role: String): String {
        return generateToken("access", id, role, Duration.ofHours(jwtConfig.accessExpiration))
    }

    fun generateRefreshToken(id: Long, role: String): String {
        return generateToken("refresh", id, role, Duration.ofHours(jwtConfig.refreshExpiration))
    }


    private fun generateToken(subject: String, id: Long, role: String, duration: Duration): String {
        val claims = Jwts.claims()
            .add(mapOf("subject" to subject, "id" to id, "role" to role)).build()

        val key = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8))
        val now = Instant.now()

        return Jwts.builder()
            .subject(subject)
            .issuer(jwtConfig.issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(duration)))
            .claims(claims)
            .signWith(key)
            .compact()
    }
}