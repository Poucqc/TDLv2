package com.jb.tdl2.security.verifyEmail

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import java.time.Duration

class RedisVerifyEmail(
    private val redisTemplate: StringRedisTemplate,
    private val emailSender: JavaMailSender
) {

    fun generateVerificationCode(): String {
        val chars = ('A'..'Z') + ('0'..'9')
        return (1..6).map { chars.random() }.joinToString("")
    }

    fun saveVerificationCodeToRedis(email: String, code: String) {
        val expiration = Duration.ofMinutes(10)
        redisTemplate.opsForValue().set(email, code, expiration)
    }

    fun sendVerificationEmail(email: String, code: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.subject = "Email Verification Code"
        message.text = "Your verification code is $code"
        emailSender.send(message)
    }
}