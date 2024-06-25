package com.jb.tdl2.domain.exception

data class InvalidTokenException(
    val provider: String?,
) : RuntimeException("$provider token is not invalid.")
