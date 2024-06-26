package com.jb.tdl2.domain.post.controller

import com.jb.tdl2.domain.post.dto.PostListResponse
import com.jb.tdl2.domain.post.dto.PostResponse
import com.jb.tdl2.domain.post.dto.SearchPostRequest
import com.jb.tdl2.domain.post.dto.UpdatePostRequest
import com.jb.tdl2.domain.post.model.Post
import com.jb.tdl2.domain.post.service.PostService
import com.jb.tdl2.security.CustomAuth
import com.jb.tdl2.security.GetCurrentId.getCurrentId
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @CustomAuth(roles = ["user"])
    @PostMapping
    fun createPost(
        @RequestBody @Validated request: UpdatePostRequest,
    ): ResponseEntity<PostResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(postService.createPost(currentId, request))
    }

    @CustomAuth(roles = ["user"])
    @PutMapping("/{post-id}")
    fun updatePost(
        @PathVariable("post-id") postId: Long,
        @RequestBody @Validated request: UpdatePostRequest
    ): ResponseEntity<PostResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.updatePost(postId, currentId, request))
    }

    @CustomAuth(roles = ["user"])
    @DeleteMapping("/{post-id}")
    fun deletePost(
        @PathVariable("post-id") postId: Long,
    ): ResponseEntity<Unit> {
        val currentId = getCurrentId()
        postService.deletePost(postId, currentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build()
    }


    @GetMapping("/list")
    fun getPostList(
        pageable: Pageable,
    ): ResponseEntity<List<PostListResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPostsList(pageable))
    }

    @GetMapping("/{post-id}")
    fun getPost(
        @PathVariable("post-id") postId: Long,
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPost(postId))
    }

    @GetMapping("/search=?")
    fun searchPosts(
        @ModelAttribute request: SearchPostRequest,
        pageable: Pageable,
    ): ResponseEntity<List<PostListResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.searchPosts(request, pageable))
    }
}