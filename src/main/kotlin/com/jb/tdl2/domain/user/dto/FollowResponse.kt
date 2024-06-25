package com.jb.tdl2.domain.user.dto

data class FollowResponse(
    val followerNickname: String,
    val isFollowing: Boolean,
    val followerCount: Int,
)
