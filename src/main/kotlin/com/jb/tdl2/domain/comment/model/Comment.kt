package com.jb.tdl2.domain.comment.model

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.post.model.Post
import com.jb.tdl2.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "comments")
class Comment(

    @Column(name = "body")
    private var body: String,

    @Column(name = "created_at")
    private val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    private var updatedAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private val post: Post,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun toResponse(authorName: String): CommentResponse {
        return CommentResponse(
            id = this.id!!,
            authorId = this.user.id!!,
            authorName = authorName,
            body = this.body,
            updatedAt = this.updatedAt
        )
    }

    fun getUser() = this.user
    fun getBody() = this.body
    fun getCreatedAt() = this.createdAt
    fun getUpdatedAt() = this.updatedAt
    fun getPost() = this.post
    fun getId() = this.id

    fun updatedComment(request: String) {
        this.body = request
        this.updatedAt = LocalDateTime.now()
    }
}