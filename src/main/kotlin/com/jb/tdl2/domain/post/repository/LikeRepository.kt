package com.jb.tdl2.domain.post.repository

import com.jb.tdl2.domain.post.model.Like
import com.jb.tdl2.domain.post.model.LikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LikeRepository : JpaRepository<Like, LikeId> {

    @Query("select count(l) from Like l where l.id.postId = :postId")
    fun countLikeByPostId(postId: Long): Int

}