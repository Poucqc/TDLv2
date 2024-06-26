package com.jb.tdl2.domain.user.dto

import com.jb.tdl2.domain.user.model.User
import java.util.Date

data class ProfileResponse(
    val nickname: String,
    val joinDate: Date,
    val isBanned: Boolean,
) {
    companion object {
        fun from(user: User): ProfileResponse {
            return ProfileResponse(
                nickname = user.nickname,
                joinDate = user.joinDate,
                isBanned = user.isBanned,
            )
        }
    }
}
