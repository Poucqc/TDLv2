package com.jb.tdl2.security


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomAuth(
    val roles: Array<String>
)