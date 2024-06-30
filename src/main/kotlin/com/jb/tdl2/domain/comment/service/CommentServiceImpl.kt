package com.jb.tdl2.domain.comment.service

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.comment.dto.UpdateCommentRequest
import com.jb.tdl2.domain.comment.model.Comment
import com.jb.tdl2.domain.comment.model.DeletedComment
import com.jb.tdl2.domain.comment.repository.CommentRepository
import com.jb.tdl2.domain.comment.repository.DeletedCommentRepository
import com.jb.tdl2.domain.exception.NoPermissionException
import com.jb.tdl2.domain.exception.NotFoundException
import com.jb.tdl2.domain.post.repository.postRepository.PostRepository
import com.jb.tdl2.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val deletedCommentRepository: DeletedCommentRepository
) : CommentService {

    override fun addComment(currentId: Long, request: UpdateCommentRequest, postId: Long): CommentResponse {
        val user = userRepository.findByIdOrNull(currentId) ?: throw NotFoundException("user not found")
        user.validDeletedUser()
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("post not found")
        post.validDeletePost()

        return commentRepository.save(
            Comment(
                body = request.body,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                user = user,
                post = post
            )
        ).toResponse(user.getNickname())
    }

    @Transactional
    override fun updateComment(
        currentId: Long, request: UpdateCommentRequest, postId: Long, commentId: Long
    ): CommentResponse {
        val user = userRepository.findByIdOrNull(currentId) ?: throw NotFoundException("user not found")
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("post not found")
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundException("comment not found")

        if (user != comment.getUser()) throw NoPermissionException("comment, because not your comment")

        comment.updatedComment(request.body)

        return comment.toResponse(user.getNickname())
    }

    @Transactional
    override fun deleteComment(currentId: Long, postId: Long, commentId: Long) {
        val user = userRepository.findByIdOrNull(currentId) ?: throw NotFoundException("user not found")
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("post not found")
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundException("comment not found")

        if (user != comment.getUser()) throw NoPermissionException("comment, because not your comment")
        commentRepository.delete(comment)
        deletedCommentRepository.save(
            DeletedComment(
                body = comment.getBody(),
                user = comment.getUser(),
                post = comment.getPost(),
                createdAt = comment.getCreatedAt(),
                updatedAt = comment.getUpdatedAt(),
                deletedId = comment.getId()!!,
                deletedTime = LocalDateTime.now(),
            )
        )
    }

    override fun deleteAllExpiredComment() {
        val softDeletedComment = deletedCommentRepository.findAll() ?: emptyList()
        softDeletedComment.map { hardDeleteExpiredSoftDeletedComment(it) }
    }

    private fun hardDeleteExpiredSoftDeletedComment(deletedComment: DeletedComment) {
        val expiredDuration = deletedComment.getDeletedTime().plusDays(7)
        if (expiredDuration > LocalDateTime.now()) {
            deletedCommentRepository.delete(deletedComment)
        }
    }

}