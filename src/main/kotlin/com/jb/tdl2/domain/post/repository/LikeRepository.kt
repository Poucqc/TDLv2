package com.jb.tdl2.domain.post.repository

import com.jb.tdl2.domain.post.model.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository: JpaRepository<Like, Long> {
}