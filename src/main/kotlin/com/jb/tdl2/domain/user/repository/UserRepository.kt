package com.jb.tdl2.domain.user.repository

import com.jb.tdl2.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun existsByEmail(email: String): Boolean

    fun existsByNickname(nickname: String): Boolean

    fun findByEmail(email: String): User?

    fun findByProviderAndProviderId(provider: String, providerId: String): User?
}