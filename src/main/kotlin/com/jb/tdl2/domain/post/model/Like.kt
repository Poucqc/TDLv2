package com.jb.tdl2.domain.post.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "likes")
class Like(
    @EmbeddedId
    val id: LikeId
)