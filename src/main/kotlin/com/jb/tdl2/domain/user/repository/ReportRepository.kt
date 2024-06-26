package com.jb.tdl2.domain.user.repository

import com.jb.tdl2.domain.user.model.Report
import org.springframework.data.jpa.repository.JpaRepository

interface ReportRepository : JpaRepository<Report, Long> {

    fun findByReportIdAndReportedId(reportId: Long, reportedId: Long): List<Report>
}