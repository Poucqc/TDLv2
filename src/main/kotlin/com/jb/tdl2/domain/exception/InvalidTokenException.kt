package com.jb.tdl2.domain.exception

data class InvalidTokenException(
    val reaseon: String?,
) : RuntimeException("$reaseon token is not invalid.")
