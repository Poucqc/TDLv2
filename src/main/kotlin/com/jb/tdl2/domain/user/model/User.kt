package com.jb.tdl2.domain.user.model

import com.jb.tdl2.domain.exception.NoPermissionException
import com.jb.tdl2.domain.exception.NotFoundException
import com.jb.tdl2.domain.exception.NotMatchException
import com.jb.tdl2.security.PasswordUtils
import com.jb.tdl2.security.PasswordUtils.hashPassword
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class User(

    @Column(unique = true, nullable = false, name = "email")
    private val email: String,

    @Column(unique = true, nullable = false, name = "user_name")
    private var nickname: String,

    @Column(name = "provider")
    private val provider: String?,

    @Column(name = "provider_id")
    private val providerId: String?,

    @Column(nullable = false, name = "joined_date")
    private val joinDate: Date,

    @Column(name = "hashed_password")
    private var password: String?,

    @Column(nullable = false, name = "ban_status")
    private var isBanned: Boolean = false,

    @Column(nullable = false, name = "delete_status")
    private var isDeleted: Boolean = false,

    @Column(name = "deleted_at")
    private var deletedDate: Date? = null,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun validOAuthLoginUser() {
        if(this.provider != null) {
            throw NoPermissionException("login to social login user")
        }
    }

    fun validDeletedUser() {
        if(this.isDeleted) {
            throw NotFoundException("deleted user")
        }
    }

    fun verifyPassword(password: String, message: String) {
        if (!PasswordUtils.verifyPassword(password, this.password!!)) {
            throw NotMatchException(message)
        }
    }

    fun isBanned() = this.isBanned

    fun isDeleted() = this.isDeleted

    fun getEmail() = this.email

    fun getNickname() = this.nickname

    fun toggleBanStatus()  {
        this.isBanned = !this.isBanned
    }

    fun toggleDeleteStatus() {
        this.isDeleted = !this.isDeleted
    }

    fun changeNickname(request: String) {
        this.nickname = request
    }

    fun changePassword(request: String) {
        this.password = hashPassword(request)
    }

    fun changeDeletedDate(request: Date) {
        this.deletedDate = request
    }
}