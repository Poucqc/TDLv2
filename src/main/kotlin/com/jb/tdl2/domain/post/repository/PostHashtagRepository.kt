package com.jb.tdl2.domain.post.repository

import com.jb.tdl2.domain.post.model.PostHashtag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostHashtagRepository : JpaRepository<PostHashtag, Long> {
}