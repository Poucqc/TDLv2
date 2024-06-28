package com.jb.tdl2.domain.post.repository.postRepository

import com.jb.tdl2.infra.querydsl.QueryDslSupport
import com.jb.tdl2.domain.post.model.QPost
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl: CustomPostRepository, QueryDslSupport() {

    private val post = QPost.post
}