package com.jb.tdl2.domain.post.service

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.comment.repository.CommentRepository
import com.jb.tdl2.domain.common.hashtag.HashtagUtils.mergeAndSaveHashtags
import com.jb.tdl2.domain.exception.NoPermissionException
import com.jb.tdl2.domain.exception.NotFoundException
import com.jb.tdl2.domain.post.dto.*
import com.jb.tdl2.domain.post.model.*
import com.jb.tdl2.domain.post.repository.HashtagRepository
import com.jb.tdl2.domain.post.repository.LikeRepository
import com.jb.tdl2.domain.post.repository.PostHashtagRepository
import com.jb.tdl2.domain.post.repository.postRepository.PostRepository
import com.jb.tdl2.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

class PostServiceImpl(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val hashtagRepository: HashtagRepository,
    private val commentRepository: CommentRepository,
    private val postHashtagRepository: PostHashtagRepository
) : PostService {

    @Transactional
    override fun createPost(currentId: Long, request: UpdatePostRequest): PostResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("Not found current user : $currentId")

        val hashtags = mergeAndSaveHashtags(request.hashtag, hashtagRepository)
        val post = Post(
            title = request.title,
            content = request.content,
            user = user,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isBanned = false,
            isDeleted = false,
            viewCount = 0
        )
        postRepository.save(post)
        saveHashtagsInPost(hashtags, post)

        return post.toResponse(comments = emptyList(), likesCount = 0, hashtags = hashtags)
    }

    @Transactional
    override fun updatePost(postId: Long, currentId: Long, request: UpdatePostRequest, pageable: Pageable): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("Post not found")
        if (post.isMyPost(currentId)) throw NoPermissionException("post. postId: $postId")

        val hashtags = mergeAndSaveHashtags(request.hashtag, hashtagRepository)

        post.updatePost(request.title, request.content)
        saveHashtagsInPost(hashtags, post)

        return post.toResponse(comments = findAllComments(postId, pageable), likesCount = countLikes(postId), hashtags)
    }

    @Transactional
    override fun deletePost(postId: Long, currentId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("Post not found")
        if (post.isMyPost(currentId)) throw NoPermissionException("post. postId: $postId")
        post.softDeletePost()
    }

    override fun getPostsList(pageable: Pageable, cursor: CursorRequest): CursorPageResponse {
        val postsList = postRepository.getPostList(pageable, cursor)

        val nextCursor = if (postsList.isNotEmpty()) {
            val lastPost = postsList.last()
            lastPost.createdAt
        } else {
            null
        }

        return CursorPageResponse(postsList, nextCursor)
    }

    override fun getPost(postId: Long, pageable: Pageable): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("Post not found")
        if (post.isDeleted()) throw NotFoundException("This post is deleted")
        val hashtags = findHashtagsByPostId(postId)
        return post.toResponse(comments = findAllComments(postId, pageable), likesCount = countLikes(postId), hashtags)
    }

    override fun searchPosts(request: SearchPostRequest, pageable: Pageable, orderBy: String): List<PostListResponse> {
        return postRepository.searchPosts(request, pageable, orderBy)
    }

    @Transactional
    override fun toggleLike(currentId: Long, postId: Long): LikeResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("Post not found")
        if (!likeExist(postId, currentId)) {
            likeRepository.save(Like(id = LikeId(currentId, postId)))
        } else {
            likeRepository.delete(Like(id = LikeId(currentId, postId)))
        }
        return LikeResponse(likeExist(postId, currentId), countLikes(postId))
    }

    private fun findAllComments(postId: Long, pageable: Pageable): List<CommentResponse> {
        val comments = commentRepository.findAllByPostId(postId, pageable)
        return comments.map { it.toResponse() }
    }

    private fun countLikes(postId: Long): Int {
        return likeRepository.countLikeByPostId(postId)
    }

    private fun likeExist(postId: Long, userId: Long): Boolean {
        return likeRepository.existsById(LikeId(userId, postId))
    }

    private fun findHashtagsByPostId(postId: Long): MutableSet<Hashtag> {
        return postRepository.findHashtagsByPostId(postId).toMutableSet()
    }

    private fun saveHashtagsInPost(hashtags: Set<Hashtag>, post: Post) {
        for (hashtag in hashtags) {
            val postHashtag = PostHashtag(post = post, hashtag = hashtag)
            postHashtagRepository.save(postHashtag)
        }
    }

}