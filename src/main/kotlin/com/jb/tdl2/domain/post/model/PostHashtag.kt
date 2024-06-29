package com.jb.tdl2.domain.post.model

import jakarta.persistence.*

@Entity
@Table(name = "post_hashtag")
data class PostHashtag(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    val hashtag: Hashtag
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
