package com.jb.tdl2.domain.exception.handler

import com.jb.tdl2.domain.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidTokenException(e: InvalidTokenException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(ExpiredTokenException::class)
    fun handleExpiredTokenException(e: ExpiredTokenException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(NoPermissionException::class)
    fun handleNoPermissionException(e: NoPermissionException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(DuplicateEntityException::class)
    fun handleDuplicateEntityException(e: DuplicateEntityException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(NotMatchException::class)
    fun handleNotMatchException(e: NotMatchException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }
}