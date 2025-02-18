package com.jb.tdl2.domain.post.controller

import com.jb.tdl2.domain.post.dto.*
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
        @RequestBody @Validated request: UpdatePostRequest,
        pageable: Pageable
    ): ResponseEntity<PostResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.updatePost(postId, currentId, request, pageable))
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
        @ModelAttribute @Validated cursor: CursorRequest
    ): ResponseEntity<CursorPageResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPostsList(pageable, cursor))
    }

    @GetMapping("/{post-id}")
    fun getPost(
        @PathVariable("post-id") postId: Long,
        pageable: Pageable,
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPost(postId, pageable))
    }

    @GetMapping("/search=?")
    fun searchPosts(
        @ModelAttribute request: SearchPostRequest,
        pageable: Pageable,
        @RequestParam orderBy: String,
    ): ResponseEntity<List<PostListResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.searchPosts(request, pageable, orderBy))
    }

    @CustomAuth(roles = ["user"])
    @PatchMapping("/like/{post-id}")
    fun toggleLike(
        @PathVariable("post-id") postId: Long,
    ): ResponseEntity<LikeResponse> {
        val currentId = getCurrentId()
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.toggleLike(currentId, postId))
    }
}