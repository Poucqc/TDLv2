package com.jb.tdl2.domain.exception

data class ExpiredTokenException(
    val token: String,
): RuntimeException("$token is expired")