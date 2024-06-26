package com.jb.tdl2.domain.exception

data class NotMatchException(
    val model: String,
) : RuntimeException("$model is not match")
