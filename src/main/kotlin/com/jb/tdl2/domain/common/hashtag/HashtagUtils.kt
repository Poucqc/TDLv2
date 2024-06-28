package com.jb.tdl2.domain.common.hashtag

import com.jb.tdl2.domain.post.model.Hashtag
import com.jb.tdl2.domain.post.repository.HashtagRepository

object HashtagUtils {

    fun mergeAndSaveHashtags(requestHashtags: Set<String>, hashtagRepository: HashtagRepository): MutableSet<Hashtag> {
        val existingHashtagsMap = hashtagRepository.findByNameIn(requestHashtags).associateBy { it.name }

        val result = mutableSetOf<Hashtag>()
        val newHashtags = mutableListOf<Hashtag>()

        for (hashtagName in requestHashtags) {
            val existingHashtag = existingHashtagsMap[hashtagName]
            if (existingHashtag != null) {
                result.add(existingHashtag)
            } else {
                val newHashtag = Hashtag(name = hashtagName)
                newHashtags.add(newHashtag)
                result.add(newHashtag)
            }
        }

        if (newHashtags.isNotEmpty()) {
            val savedNewHashtags = hashtagRepository.saveAll(newHashtags)
            result.addAll(savedNewHashtags)
        }

        return result.toMutableSet()
    }
}