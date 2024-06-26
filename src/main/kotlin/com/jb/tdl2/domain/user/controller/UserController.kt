package com.jb.tdl2.domain.user.controller

import com.jb.tdl2.domain.user.dto.*
import com.jb.tdl2.domain.user.service.UserService
import com.jb.tdl2.security.CustomAuth
import com.jb.tdl2.security.GetCurrentId.getCurrentId
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<LoginResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.login(request))
    }

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest,
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.register(request))
    }

    @CustomAuth(roles = ["user"])
    @GetMapping("/profile/{userId}")
    fun getProfile(
        @PathVariable userId: Long,
    ): ResponseEntity<ProfileResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getProfile(userId))
    }

    @CustomAuth(roles = ["user"])
    @PutMapping("/update")
    fun profileUpdate(
        @RequestParam password: String,
        @RequestBody request: ProfileUpdateRequest,
    ): ResponseEntity<UserResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.profileUpdate(password, request, currentId))
    }

    @CustomAuth(roles = ["user"])
    @DeleteMapping("/unregister")
    fun unregister(
        @RequestParam password: String,
    ): ResponseEntity<Unit> {
        val currentId = getCurrentId()
        userService.unregister(currentId, password)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build()
    }

    @CustomAuth(roles = ["user"])
    @PatchMapping("/follow/{userId}")
    fun followUser(
        @PathVariable userId: Long,
    ): ResponseEntity<FollowResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.followUser(userId, currentId))
    }

    @CustomAuth(roles = ["user"])
    @PatchMapping("/unfollow/{userId}")
    fun unfollowUser(
        @PathVariable userId: Long,
    ): ResponseEntity<FollowResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.unfollowUser(userId, currentId))
    }

    @CustomAuth(roles = ["user"])
    @PatchMapping("/report/{userId}")
    fun reportUser(
        @PathVariable userId: Long,
    ): ResponseEntity<Unit> {
        val currentId = getCurrentId()
        userService.reportUser(userId, currentId)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

}