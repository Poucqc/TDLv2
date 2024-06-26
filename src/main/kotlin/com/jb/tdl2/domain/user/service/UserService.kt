package com.jb.tdl2.domain.user.service

import com.jb.tdl2.domain.auth.dto.UserInfoResponse
import com.jb.tdl2.domain.user.dto.*
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun login(request: LoginRequest): LoginResponse

    fun register(request: RegisterRequest): UserResponse

    fun verifyEmail(currentId: Long,verifyCode: String): UserResponse

    fun registerUserIfAbsent(userInfo: UserInfoResponse, provider: String): UserResponse

    fun getMyProfile(currentId: Long): MyProfileResponse

    fun getProfile(userId: Long): ProfileResponse

    fun profileUpdate(request: ProfileUpdateRequest, currentId: Long): UserResponse

    fun passwordChange(request: PasswordChangeRequest, currentId: Long): UserResponse

    fun unregister(currentId: Long, password: String)

    fun followUser(userId: Long, currentId: Long): FollowResponse

    fun unfollowUser(userId: Long, currentId: Long): FollowResponse

    fun reportUser(userId: Long, currentId: Long): ReportResponse


}