package com.jb.tdl2.domain.comment.repository

import com.jb.tdl2.domain.comment.model.DeletedComment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeletedCommentRepository: JpaRepository<DeletedComment, Long> {
}