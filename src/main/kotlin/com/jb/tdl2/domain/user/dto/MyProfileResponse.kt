package com.jb.tdl2.domain.user.dto

import com.jb.tdl2.domain.user.model.User
import java.util.*

data class MyProfileResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val joinDate: Date,
    val provider: String?,
    val providerId: String?,
    val reportCount: Int,
    val followerCount: Int,
    val followingList: List<UserResponse>
) {
    companion object {
        fun from(user: User, followerCount: Int, followingList: List<UserResponse>): MyProfileResponse {
            return MyProfileResponse(
                id = user.id!!,
                nickname = user.nickname,
                email = user.email,
                joinDate = user.joinDate,
                provider = user.provider,
                providerId = user.providerId,
                reportCount = user.reportCount,
                followerCount =  followerCount,
                followingList = followingList
            )
        }
    }
}
