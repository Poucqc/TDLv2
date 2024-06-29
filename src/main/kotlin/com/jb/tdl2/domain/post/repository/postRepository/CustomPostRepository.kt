package com.jb.tdl2.domain.post.repository.postRepository

import com.jb.tdl2.domain.post.dto.CursorRequest
import com.jb.tdl2.domain.post.dto.PostListResponse
import com.jb.tdl2.domain.post.dto.SearchPostRequest
import com.jb.tdl2.domain.post.model.Hashtag
import org.springframework.data.domain.Pageable

interface CustomPostRepository {
    
    fun getPostList(pageable: Pageable, cursor: CursorRequest): List<PostListResponse>

    fun searchPosts(request: SearchPostRequest, pageable: Pageable, orderBy: String): List<PostListResponse>

    fun findHashtagsByPostId(postId: Long): Set<Hashtag>
}