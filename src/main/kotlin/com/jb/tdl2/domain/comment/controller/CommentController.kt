package com.jb.tdl2.domain.comment.controller

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.comment.dto.UpdateCommentRequest
import com.jb.tdl2.domain.comment.service.CommentService
import com.jb.tdl2.security.CustomAuth
import com.jb.tdl2.security.GetCurrentId.getCurrentId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/{post-id}/comment")
class CommentController(
    private val commentService: CommentService
) {

    @CustomAuth(roles = ["user"])
    @PostMapping("/add")
    fun addComment(
        @PathVariable("post-id") postId: Long,
        @RequestBody request: UpdateCommentRequest,
    ): ResponseEntity<CommentResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.addComment(currentId, request, postId))
    }

    @CustomAuth(roles = ["user"])
    @PatchMapping("/{comment-id}/update")
    fun updateComment(
        @PathVariable("post-id") postId: Long,
        @PathVariable("comment-id") commentId: Long,
        @RequestBody request: UpdateCommentRequest,
    ): ResponseEntity<CommentResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(currentId, request, postId, commentId))
    }

    @CustomAuth(roles = ["user"])
    @DeleteMapping("/{comment-id}")
    fun deleteComment(
        @PathVariable("comment-id") commentId: Long,
        @PathVariable("post-id") postId: Long,
    ): ResponseEntity<Unit> {
        val currentId = getCurrentId()
        commentService.deleteComment(currentId, postId, commentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build()
    }

}