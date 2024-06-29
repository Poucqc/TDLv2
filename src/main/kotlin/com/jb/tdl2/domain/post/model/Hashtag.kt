package com.jb.tdl2.domain.post.model

import jakarta.persistence.*

@Entity
@Table(name = "Hashtags")
class Hashtag(

    @Column(name = "name", nullable = false)
    val name: String,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}