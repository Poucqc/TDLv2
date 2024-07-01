package com.jb.tdl2.domain.comment.service

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.comment.dto.UpdateCommentRequest
import org.springframework.stereotype.Service

@Service
interface CommentService {

    fun addComment(currentId: Long, request: UpdateCommentRequest, postId: Long): CommentResponse

    fun updateComment(currentId: Long, request: UpdateCommentRequest, postId: Long, commentId: Long): CommentResponse

    fun deleteComment(currentId: Long, postId: Long, commentId: Long)

    fun deleteAllExpiredComment()

}