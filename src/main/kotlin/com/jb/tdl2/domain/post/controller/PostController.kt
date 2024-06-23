package com.jb.tdl2.domain.post.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @PostMapping
    fun createPost(
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(postService.createPost())
    }

    @PutMapping("/{post-id}")
    fun updatePost(
        @PathVariable("post-id") postId: Long,
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.updatePost())
    }

    @DeleteMapping("/{post-id}")
    fun deletePost(
        @PathVariable("post-id") postId: Long,
    ) {
        postService.deletePost()
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build()
    }

    @GetMapping("/list")
    fun getPostList(
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPostList())
    }

    @GetMapping("/{post-id}")
    fun getPost(
        @PathVariable("post-id") postId: Long,
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPost())
    }

    @GetMapping("/search=?")
    fun searchPost(
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.searchPost())
    }
}