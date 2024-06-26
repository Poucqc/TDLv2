package com.jb.tdl2.domain.user.repository

import com.jb.tdl2.domain.user.model.Follow
import com.jb.tdl2.domain.user.model.FollowId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository: JpaRepository<Follow, Long>  {

    fun existByFollowId(followId: FollowId): Boolean

    fun findById(followId: FollowId): Follow?

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.id.userId = :userId")
    fun countByUserId(@Param("userId") userId: Long): Int

    @Query("SELECT f.id.userId FROM Follow f WHERE f.id.followerId = :followerId")
    fun findByFollowerId(@Param("followerId") followerId: Long): List<Long>
}