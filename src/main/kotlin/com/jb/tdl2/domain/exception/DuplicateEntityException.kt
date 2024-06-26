package com.jb.tdl2.domain.exception

data class DuplicateEntityException(
    val entity: String
): RuntimeException("$entity is already exist.")
