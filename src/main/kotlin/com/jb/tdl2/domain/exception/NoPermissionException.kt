package com.jb.tdl2.domain.exception

data class NoPermissionException(
    val model: String
): RuntimeException("No permission for $model")
