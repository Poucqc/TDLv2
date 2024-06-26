package com.jb.tdl2.domain.comment.model

import com.jb.tdl2.domain.user.model.User
import jakarta.persistence.*


@Entity
@Table(name = "comments")
class Comment(

    var content: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}