package com.jb.tdl2.domain.post.repository.PostRepository

import com.jb.tdl2.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>, CustomPostRepository {
}