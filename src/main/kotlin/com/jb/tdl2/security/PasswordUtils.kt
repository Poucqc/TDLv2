package com.jb.tdl2.security

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {

    fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    fun verifyPassword(inputPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(inputPassword, hashedPassword)
    }
}