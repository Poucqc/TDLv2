package com.jb.tdl2.domain.post.repository

import com.jb.tdl2.domain.post.model.Hashtag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HashtagRepository: JpaRepository<Hashtag, Long> {

    fun findByNameIn(names: Set<String>): Set<Hashtag>
}