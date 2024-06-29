package com.jb.tdl2.domain.user.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class User(

    @Column(unique = true, nullable = false, name = "email")
    val email: String,

    @Column(unique = true, nullable = false, name = "user_name")
    var nickname: String,

    @Column(name = "provider")
    val provider: String?,

    @Column(name = "provider_id")
    val providerId: String?,

    @Column(nullable = false, name = "joined_date")
    val joinDate: Date,

    @Column(name = "hashed_password")
    var password: String?,

    @Column(nullable = false, name = "ban_status")
    var isBanned: Boolean = false,

    @Column(nullable = false, name = "delete_status")
    var deleted: Boolean = false,

    @Column(name = "deleted_at")
    var deletedDate: Date? = null,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}