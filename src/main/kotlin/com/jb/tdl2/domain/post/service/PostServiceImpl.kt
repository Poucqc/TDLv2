package com.jb.tdl2.domain.post.service

import com.jb.tdl2.domain.comment.dto.CommentResponse
import com.jb.tdl2.domain.comment.repository.CommentRepository
import com.jb.tdl2.domain.common.hashtag.HashtagUtils.mergeAndSaveHashtags
import com.jb.tdl2.domain.exception.NoPermissionException
import com.jb.tdl2.domain.exception.NotFoundException
import com.jb.tdl2.domain.post.dto.*
import com.jb.tdl2.domain.post.model.Like
import com.jb.tdl2.domain.post.model.LikeId
import com.jb.tdl2.domain.post.model.Post
import com.jb.tdl2.domain.post.repository.HashtagRepository
import com.jb.tdl2.domain.post.repository.LikeRepository
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
) : PostService {

    @Transactional
    override fun createPost(currentId: Long, request: UpdatePostRequest): PostResponse {
        val user = userRepository.findByIdOrNull(currentId)
            ?: throw NotFoundException("Not found current user : $currentId")
        val hashtag = mergeAndSaveHashtags(request.hashtag, hashtagRepository)
        return postRepository.save(
            Post(
                title = request.title,
                content = request.content,
                user = user,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                isBanned = false,
                isDeleted = false,
                viewCount = 0,
                hashtags = hashtag
            )
        ).toResponse(comments = emptyList(), likesCount = 0)
    }

    @Transactional
    override fun updatePost(postId: Long, currentId: Long, request: UpdatePostRequest, pageable: Pageable): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("Post not found")
        if (post.isMyPost(currentId)) throw NoPermissionException("post. postId: $postId")
        val hashtags = mergeAndSaveHashtags(request.hashtag, hashtagRepository)

        post.updatePost(request.title, request.content, hashtags)
        return post.toResponse(comments = findAllComments(postId, pageable), likesCount = countLikes(postId))
    }

    @Transactional
    override fun deletePost(postId: Long, currentId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("Post not found")
        if (post.isMyPost(currentId)) throw NoPermissionException("post. postId: $postId")
        post.softDeletePost()
    }

    override fun getPostsList(pageable: Pageable): List<PostListResponse> {
        // TODO: queryDSL을 사용해 projections contstructor PostListResponse 형태로 pagenation 된 정보 가져오기
        // TODO: orderby 를 동적으로 만들어서 request 에 해당하는 orderby 적용
        TODO()
    }

    override fun getPost(postId: Long, pageable: Pageable): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException("Post not found")
        if (post.isDeleted()) throw NotFoundException("This post is deleted")
        return post.toResponse(comments = findAllComments(postId, pageable), likesCount = countLikes(postId))
    }

    override fun searchPosts(request: SearchPostRequest, pageable: Pageable): List<PostListResponse> {
        // TODO: queryDSL을 사용해 projections contstructor PostListResponse 형태로 pagenation 된 정보 가져오기
        // TODO: search 에 해당하는 builder 를 만들어서 where 조건 적용시키기
        // TODO: orderby 를 동적으로 만들어서 request 에 해당하는 orderby 적용
        // TODO: 알고리즘 대신 search에 해당하는 단어가 포함된 횟수를 count() 해서 그 순위별로 orderby 할 수 있을까?
        TODO()
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
}