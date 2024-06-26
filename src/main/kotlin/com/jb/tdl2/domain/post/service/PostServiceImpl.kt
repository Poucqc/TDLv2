package com.jb.tdl2.domain.post.service

import com.jb.tdl2.domain.post.dto.PostListResponse
import com.jb.tdl2.domain.post.dto.PostResponse
import com.jb.tdl2.domain.post.dto.SearchPostRequest
import com.jb.tdl2.domain.post.dto.UpdatePostRequest
import org.springframework.data.domain.Pageable

class PostServiceImpl: PostService {
    override fun createPost(userId: Long, request: UpdatePostRequest): PostResponse {
        TODO("Not yet implemented")
    }

    override fun updatePost(postId: Long, userId: Long, request: UpdatePostRequest): PostResponse {
        TODO("Not yet implemented")
    }

    override fun deletePost(postId: Long, userId: Long) {
        TODO("Not yet implemented")
    }

    override fun getPostsList(pageable: Pageable): List<PostListResponse> {
        TODO("Not yet implemented")
    }

    override fun getPost(postId: Long): PostResponse {
        TODO("Not yet implemented")
    }

    override fun searchPosts(request: SearchPostRequest, pageable: Pageable): List<PostListResponse> {
        TODO("Not yet implemented")
    }

    override fun likeToPost(currentId: Long, postId: Long): PostResponse {
        TODO("Not yet implemented")
    }
}