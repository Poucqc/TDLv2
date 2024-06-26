package com.jb.tdl2.domain.post.service

import com.jb.tdl2.domain.post.dto.PostListResponse
import com.jb.tdl2.domain.post.dto.PostResponse
import com.jb.tdl2.domain.post.dto.SearchPostRequest
import com.jb.tdl2.domain.post.dto.UpdatePostRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface PostService {

    fun createPost(userId: Long, request: UpdatePostRequest): PostResponse

    fun updatePost(postId: Long, userId: Long, request: UpdatePostRequest): PostResponse

    fun deletePost(postId: Long, userId: Long)

    fun getPostsList(pageable: Pageable): List<PostListResponse>

    fun getPost(postId: Long): PostResponse

    fun searchPosts(request: SearchPostRequest, pageable: Pageable): List<PostListResponse>

    fun likeToPost(currentId: Long, postId: Long): PostResponse
}