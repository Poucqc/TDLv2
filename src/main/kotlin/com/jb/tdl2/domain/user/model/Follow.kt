package com.jb.tdl2.domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "follow")
class Follow (
    @EmbeddedId
    val id: FollowId,

)