package com.jb.tdl2.domain.user.service

import com.jb.tdl2.domain.auth.dto.UserInfoResponse
import com.jb.tdl2.domain.exception.DuplicateEntityException
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
        user.validOAuthLoginUser()
        user.validDeletedUser()
        user.verifyPassword(request.password, "wrong password")
        if (user.isBanned()) {
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
                isDeleted = false,
                deletedDate = null
            )
        ).let { UserResponse.from(it) }
    }

    override fun verifyEmail(currentId: Long, verifyCode: String): UserResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("user not found by currentId : $currentId")
        val codeInRedis = redisTemplate.opsForValue().get(user.getEmail())
        if (codeInRedis != verifyCode) {
            throw NotMatchException("code")
        }
        user.toggleBanStatus()
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
                    isDeleted = false,
                    deletedDate = null,
                )
            ).let { UserResponse.from(it) }
    }

    override fun getMyProfile(currentId: Long): MyProfileResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("user not found by currentId : $currentId")
        val followList = getFollowingUsers(currentId).map { UserResponse.from(it) }
        return MyProfileResponse.from(user, countFollowers(currentId), followList)
    }

    override fun getProfile(userId: Long): ProfileResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw NotFoundException("user : $userId not found")
        user.validDeletedUser()
        return ProfileResponse.from(user)
    }

    @Transactional
    override fun profileUpdate(request: ProfileUpdateRequest, currentId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("Not found current user : $currentId")
        user.validDeletedUser()
        user.changeNickname(request.nickname)
        return UserResponse.from(user)
    }

    override fun passwordChange(request: PasswordChangeRequest, currentId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("Not found current user : $currentId")
        user.validOAuthLoginUser()
        user.validDeletedUser()
        user.verifyPassword(request.currentPassword, "wrong password")
        if (request.newPassword != request.newPasswordConfirmation) {
            throw NotMatchException("password confirmation not match")
        }
        user.verifyPassword(request.newPassword, "new password must different with past password")
        user.changePassword(request.newPassword)
        return UserResponse.from(user)
    }

    override fun unregister(currentId: Long, password: String) {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("user not found by user : $currentId")
        user.validOAuthLoginUser()
        user.validDeletedUser()
        user.verifyPassword(password, "wrong password")

        user.toggleDeleteStatus()
        user.changeDeletedDate(Date.from(Instant.now()))

        userRepository.save(user)
    }

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
        val currentUser: User = userRepository.findByIdOrNull(currentId) ?: throw NotFoundException("user not found by currentId : $currentId")
        val reportedUser: User = userRepository.findByIdOrNull(currentId) ?: throw NotFoundException("user not found by userId : $userId")
        return reportRepository.save(
            Report(
                reportUser = currentUser,
                reportedUser = reportedUser,
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

    private fun countFollowers(userId: Long): Int {
        return followRepository.countByUserId(userId)
    }

    private fun getFollowingUsers(userId: Long): List<User> {
        val followingUserIds = followRepository.findByFollowerId(userId)
        return userRepository.findAllById(followingUserIds).toList()
    }

}