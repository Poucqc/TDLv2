package com.jb.tdl2.domain.user.service

import com.jb.tdl2.domain.auth.dto.UserInfoResponse
import com.jb.tdl2.domain.exception.DuplicateEntityException
import com.jb.tdl2.domain.exception.NoPermissionException
import com.jb.tdl2.domain.exception.NotFoundException
import com.jb.tdl2.domain.exception.NotMatchException
import com.jb.tdl2.domain.user.dto.*
import com.jb.tdl2.domain.user.model.Follow
import com.jb.tdl2.domain.user.model.FollowId
import com.jb.tdl2.domain.user.model.Report
import com.jb.tdl2.domain.user.model.User
import com.jb.tdl2.domain.user.repository.FollowRepository
import com.jb.tdl2.domain.user.repository.ReportRepository
import com.jb.tdl2.domain.user.repository.UserRepository
import com.jb.tdl2.security.PasswordUtils.hashPassword
import com.jb.tdl2.security.PasswordUtils.verifyPassword
import com.jb.tdl2.security.jwt.JwtPlugin
import com.jb.tdl2.security.verifyEmail.RedisVerifyEmail
import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.repository.findByIdOrNull
import java.time.Instant
import java.util.*

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val reportRepository: ReportRepository,
    private val jwtPlugin: JwtPlugin,
    private val redisVerifyEmail: RedisVerifyEmail,
    private val redisTemplate: StringRedisTemplate
) : UserService {

    override fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw NotFoundException("user not found by email : ${request.email}")
        validOAuthLoginUser(user)
        if (!verifyPassword(request.password, user.password!!)) {
            throw NotMatchException("password")
        }
        if (user.isBanned) {
            throw BadRequestException("banned user")
        }
        val accessToken = jwtPlugin.generateAccessToken(user.id!!, "user")
        val refreshToken = jwtPlugin.generateRefreshToken(user.id!!, "user")

        return LoginResponse(accessToken, refreshToken)
    }

    override fun register(request: RegisterRequest): UserResponse {
        if (!checkEmailDuplicate(request.email)) {
            throw DuplicateEntityException("email")
        }
        if (!checkNicknameDuplicate(request.nickname)) {
            throw DuplicateEntityException("nickname")
        }
        val verifyCode = redisVerifyEmail.generateVerificationCode()
        redisVerifyEmail.saveVerificationCodeToRedis(request.email, verifyCode)
        redisVerifyEmail.sendVerificationEmail(request.email, verifyCode)

        return userRepository.save(
            User(
                email = request.email,
                nickname = request.nickname,
                joinDate = Date.from(Instant.now()),
                password = hashPassword(request.password),
                provider = null,
                providerId = null,
                isBanned = true,
                deleted = false,
                deletedDate = null
            )
        ).let { UserResponse.from(it) }
    }

    override fun verifyEmail(currentId: Long, verifyCode: String): UserResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("user not found by currentId : $currentId")
        val codeInRedis = redisTemplate.opsForValue().get(user.email)
        if (codeInRedis != verifyCode) {
            throw NotMatchException("code")
        }
        user.isBanned = false
        return userRepository.save(user).let { UserResponse.from(it) }
    }

    override fun registerUserIfAbsent(userInfo: UserInfoResponse, provider: String): UserResponse {
        return userRepository.findByProviderAndProviderId(provider, userInfo.id)?.let { UserResponse.from(it) }
            ?: userRepository.save(
                User(
                    email = userInfo.email,
                    nickname = userInfo.nickname,
                    provider = provider,
                    providerId = userInfo.id,
                    joinDate = Date.from(Instant.now()),
                    password = null,
                    isBanned = false,
                    deleted = false,
                    deletedDate = null,
                )
            ).let { UserResponse.from(it) }
    }

    override fun getProfile(userId: Long): ProfileResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw NotFoundException("user : $userId not found")
        return ProfileResponse.from(user)
    }

    @Transactional
    override fun profileUpdate(request: ProfileUpdateRequest, currentId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("Not found current user : $currentId")
        user.nickname = request.nickname
        return UserResponse.from(user)
    }

    override fun passwordChange(request: PasswordChangeRequest, currentId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("Not found current user : $currentId")
        validOAuthLoginUser(user)
        if (!verifyPassword(request.currentPassword, user.password!!)) {
            throw NotMatchException("password")
        }
        if (request.newPassword != request.newPasswordConfirmation) {
            throw NotMatchException("password confirmation")
        }
        if (verifyPassword(request.newPassword, user.password!!)) {
            throw DuplicateEntityException("password")
        }
        user.password = hashPassword(request.newPassword)
        return UserResponse.from(user)
    }

    override fun unregister(currentId: Long, password: String) {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("user not found by user : $currentId")
        if (user.provider == null) {
            if (!verifyPassword(password, user.password!!)) {
                throw NotMatchException("password")
            }
        }
        user.deleted = true
        user.deletedDate = Date.from(Instant.now())

        userRepository.save(user)
    }

    @Transactional
    override fun followUser(userId: Long, currentId: Long): FollowResponse {
        val follower = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("user not found by user : $currentId")
        val user = userRepository.findByIdOrNull(userId)
            ?: throw NotFoundException("user not found by user : $userId")

        val follow = followRepository.save(
            Follow(
                FollowId(
                    followerId = currentId,
                    userId = userId
                )
            )
        )
        return FollowResponse(getFollowStatus(currentId, userId))
    }

    override fun unfollowUser(userId: Long, currentId: Long): FollowResponse {
        val follow = followRepository.findById(FollowId(currentId, userId))
            ?: throw NotFoundException("Not followed yet")
        followRepository.delete(follow)
        return FollowResponse(getFollowStatus(currentId, userId))
    }

    override fun reportUser(userId: Long, currentId: Long): ReportResponse {
        checkReportDuplicate(currentId, userId)
        return reportRepository.save(
            Report(
                reportId = currentId,
                reportedId = userId,
                reportDate = Date.from(Instant.now()),
            )
        ).let { ReportResponse.from(it) }
    }

    private fun checkEmailDuplicate(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    private fun checkNicknameDuplicate(nickname: String): Boolean {
        return userRepository.existsByNickname(nickname)
    }

    private fun getFollowStatus(followerId: Long, userId: Long): Boolean {
        return followRepository.existByFollowId(FollowId(followerId, userId))
    }

    private fun checkReportDuplicate(currentId: Long, reportedId: Long) {
        val reports = reportRepository.findByReportIdAndReportedId(currentId, reportedId)
        reports.map {
            if (it.reportDate == Date.from(Instant.now())) {
                throw DuplicateEntityException("report date")
            }
        }
    }

    private fun validOAuthLoginUser(user: User) {
        if (user.provider != null) {
            throw NoPermissionException("login to social login user")
        }
    }

}