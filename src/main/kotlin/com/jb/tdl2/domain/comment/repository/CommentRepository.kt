package com.jb.tdl2.domain.comment.repository

import com.jb.tdl2.domain.comment.model.Comment
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    fun findAllByPostId(postId: Long, pageable: Pageable): List<Comment>
}