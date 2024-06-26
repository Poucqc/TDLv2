package com.jb.tdl2.domain.post.model

import com.jb.tdl2.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class Post(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "create_time")
    val createdAt: LocalDateTime,

    @Column(name = "update_time")
    var updatedAt: LocalDateTime,

    @Column(name = "view_count")
    var viewCount: Int,

    @ManyToMany()
    @JoinTable(
        name = "post_hashtag",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "hashtag_id")]
    )
    val hashtags: MutableSet<Hashtag> = mutableSetOf(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "banned")
    val isBanned: Boolean,

    @Column(name = "deleted")
    val isDeleted: Boolean,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
