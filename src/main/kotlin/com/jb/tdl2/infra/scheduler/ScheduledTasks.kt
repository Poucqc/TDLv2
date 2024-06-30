package com.jb.tdl2.infra.scheduler

import com.jb.tdl2.domain.comment.service.CommentService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.scheduler.Scheduler
import java.time.LocalDateTime

@Component
class ScheduledTasks(
    private val commentService: CommentService,
) {

    private val logger = LoggerFactory.getLogger(ScheduledTasks::class.java)


    @Scheduled(cron = "0 0 4 * * ?")
    fun deletedCommentScheduledTasks() {
        commentService.deleteCommentScheduler()
        logger.info("${LocalDateTime.now()} 에 deletedComment 를 실행했습니다")
    }
}