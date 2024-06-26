package com.jb.tdl2.domain.user.dto

import com.jb.tdl2.domain.user.model.User

data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val isBanned: Boolean,
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                user.id!!,
                user.email,
                user.nickname,
                user.isBanned,
            )
        }
    }
}
