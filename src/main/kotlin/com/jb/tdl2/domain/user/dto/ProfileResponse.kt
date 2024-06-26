package com.jb.tdl2.domain.user.dto

import com.jb.tdl2.domain.user.model.User

data class ProfileResponse(
    val nickname: String,
) {
    companion object {
        fun from(user: User): ProfileResponse {
            return ProfileResponse(
                nickname = user.nickname,
            )
        }
    }
}
