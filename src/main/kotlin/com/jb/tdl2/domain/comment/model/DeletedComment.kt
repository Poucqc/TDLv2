package com.jb.tdl2.domain.comment.model

import com.jb.tdl2.domain.post.model.Post
import com.jb.tdl2.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "deleted_comment")
class DeletedComment(

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

    private val deletedTime: LocalDateTime,

    private val deletedId: Long
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}