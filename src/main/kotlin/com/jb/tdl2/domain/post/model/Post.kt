package com.jb.tdl2.domain.post.model

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.exception.NotFoundException
import com.jb.tdl2.domain.post.dto.PostResponse
import com.jb.tdl2.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class Post(
    @Column(name = "title")
    private var title: String,

    @Column(name = "content")
    private var content: String,

    @Column(name = "create_time")
    private val createdAt: LocalDateTime,

    @Column(name = "update_time")
    private var updatedAt: LocalDateTime,

    @Column(name = "view_count")
    private var viewCount: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private val user: User,

    @Column(name = "banned")
    private var isBanned: Boolean,

    @Column(name = "deleted")
    private var isDeleted: Boolean,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun toResponse(comments: List<CommentResponse>, likesCount: Int, hashtags: MutableSet<Hashtag>, username: String): PostResponse {
        return PostResponse(
            id = this.id!!,
            title = this.title,
            content = this.content,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            hashtag = hashtags,
            comments = comments,
            authorId = this.user.id!!,
            authorName = username,
            likesCount = likesCount,
            viewCount = this.viewCount,
        )
    }

    fun updatePost(title: String, content: String) {
        this.title = title
        this.content = content
        updatedAt = LocalDateTime.now()
    }

    fun softDeletePost() {
        this.isDeleted = true
        println("${this.id} post successfully deleted")
    }

    fun validDeletePost() {
        if(this.isDeleted) throw NotFoundException("deleted post")
    }

    fun isBanned(): Boolean = this.isBanned

    fun isDeleted(): Boolean = this.isDeleted

    fun isMyPost(currentId: Long): Boolean {
        return this.user.id == currentId
    }

    fun getAuthor() = this.user

}
