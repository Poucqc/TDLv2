package com.jb.tdl2.domain.post.repository.PostRepository

import com.jb.tdl2.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface PostRepository: JpaRepository<Post, Long>, CustomPostRepository {
}