package com.jb.tdl2.domain.post.model

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class LikeId(
    val userId: Long,
    val postId: Long
) : Serializable