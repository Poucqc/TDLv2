package com.jb.tdl2.domain.user.repository

import com.jb.tdl2.domain.user.model.Follow
import com.jb.tdl2.domain.user.model.FollowId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository: JpaRepository<Follow, Long>  {

    fun existByFollowId(followId: FollowId): Boolean

    fun findById(followId: FollowId): Follow?
}