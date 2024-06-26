package com.jb.tdl2.security

import com.jb.tdl2.domain.exception.NotFoundException
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object GetCurrentId {
    fun getCurrentId(): Long {
        val attributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
            ?: throw RuntimeException("Missing request attributes")

        val request = attributes.request
        val userId = request.getAttribute("userId") as? Long ?: throw NotFoundException("Missing request attribute")
        return userId
    }
}