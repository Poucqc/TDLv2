package com.jb.tdl2.domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class FollowId(

    //팔로우 하는 사람
    @Column(name ="follower_id")
    val followerId: Long,

    //팔로우 받는 사람
    @Column(name ="user_id")
    val userId: Long
) : Serializable