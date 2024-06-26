package com.jb.tdl2.domain.user.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "report")
class Report(

    val reportId: Long,

    val reportedId: Long,

    val reportDate: Date,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}