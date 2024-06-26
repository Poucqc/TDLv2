package com.jb.tdl2.security

import com.jb.tdl2.domain.exception.ExpiredTokenException
import com.jb.tdl2.domain.exception.InvalidTokenException
import com.jb.tdl2.domain.exception.NoPermissionException
import com.jb.tdl2.security.jwt.JwtPlugin
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

@Aspect
@Component
class CustomAuthAspect(
    private val jwtPlugin: JwtPlugin
) {

    @Pointcut("@annotation(customAuth)")
    fun customAuthPointCut(customAuth: CustomAuth) {
    }

    @Before("customAuthPointCut(customAuth)")
    fun customAuthAdvice(customAuth: CustomAuth) {
        val attributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
            ?: throw RuntimeException("Missing request attributes")
        val request: HttpServletRequest = attributes.request

        val authHeader = request.getHeader("Authorization")
            ?: throw RuntimeException("Missing Authorization header.")
        if (!authHeader.startsWith("Bearer ")) {
            throw RuntimeException("Invalid Authorization header.")
        }

        val token = authHeader.substring(7)
        val decodedToken = jwtPlugin.validateToken(token)
            ?: throw InvalidTokenException("")

        decodedToken.onSuccess { accessToken ->
            if (accessToken.payload.subject != "access") throw InvalidTokenException("access")

            if (accessToken.payload.expiration.before(Date())) {
                val refreshHeader = request.getHeader("Refresh token")
                    ?: throw InvalidTokenException("Refresh")

                val refreshToken: String = refreshHeader.substring(7)
                val decodedRefreshToken = jwtPlugin.validateToken(refreshToken)

                decodedRefreshToken.onSuccess { refreshToken ->
                    if (refreshToken.payload.subject != "refresh") throw InvalidTokenException("refresh")
                    if (refreshToken.payload.expiration.before(Date())) throw ExpiredTokenException("refresh")

                    val newAccessToken = jwtPlugin.generateAccessToken(
                        refreshToken.payload["id"].toString().toLong(),
                        refreshToken.payload["role"].toString()
                    )
                    val newRefreshToken = jwtPlugin.generateRefreshToken(
                        refreshToken.payload["id"].toString().toLong(),
                        refreshToken.payload["role"].toString()
                    )

                    val response = attributes.response
                    response?.setHeader("Authorization", "Bearer $newAccessToken")
                    response?.setHeader("refresh-token", newRefreshToken)

                    val newDecodedToken = jwtPlugin.validateToken(newAccessToken)
                        ?: throw InvalidTokenException("access")

                    newDecodedToken.onSuccess { newAccessToken ->
                        if (newAccessToken.payload.subject != "access") throw InvalidTokenException("access")
                        if (newAccessToken.payload.expiration.before(Date())) throw ExpiredTokenException("refresh")
                    }.onFailure {
                        throw InvalidTokenException("access")
                    }
                }.onFailure {
                    throw InvalidTokenException("refresh")
                }
            } else {
                validateRoles(accessToken.payload["role"].toString(), customAuth)
            }
        }.onFailure {
            throw InvalidTokenException("access")
        }

    }

    private fun validateRoles(tokenRole: String, customAuth: CustomAuth) {
        if (tokenRole !in customAuth.roles) {
            throw NoPermissionException("No permission to access this resource")
        }
    }

}