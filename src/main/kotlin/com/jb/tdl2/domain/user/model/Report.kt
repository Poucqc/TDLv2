package com.jb.tdl2.domain.user.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "report")
class Report(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_user_id", nullable = false)
    val reportUser: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    val reportedUser: User,

    val reportDate: Date,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}