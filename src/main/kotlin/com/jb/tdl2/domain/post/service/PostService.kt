package com.jb.tdl2.domain.post.service

import com.jb.tdl2.domain.post.dto.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface PostService {

    fun createPost(currentId: Long, request: UpdatePostRequest): PostResponse

    fun updatePost(postId: Long, currentId: Long, request: UpdatePostRequest, pageable: Pageable): PostResponse

    fun deletePost(postId: Long, currentId: Long)

    fun getPostsList(pageable: Pageable, cursor: CursorRequest): CursorPageResponse

    fun getPost(postId: Long, pageable: Pageable): PostResponse

    fun searchPosts(request: SearchPostRequest, pageable: Pageable, orderBy: String): List<PostListResponse>

    fun toggleLike(currentId: Long, postId: Long): LikeResponse
}