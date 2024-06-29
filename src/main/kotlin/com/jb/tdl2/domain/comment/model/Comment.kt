package com.jb.tdl2.domain.comment.model

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.post.model.Post
import com.jb.tdl2.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "comments")
class Comment(

    @Column(name = "conents")
    var body: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun toResponse(): CommentResponse {
        return CommentResponse(
            id = this.id!!,
            authorId = this.user.id!!,
            authorName = this.user.nickname,
            body = this.body,
            updatedAt = this.updatedAt
        )
    }
}