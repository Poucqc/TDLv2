package com.jb.tdl2.domain.user.dto

import com.jb.tdl2.domain.user.model.Report
import java.util.Date

data class ReportResponse(
    val reportId: Long,
    val reportedId: Long,
    val reportDate: Date
) {
    companion object {
        fun from(report: Report): ReportResponse {
            return ReportResponse(
                report.reportId,
                report.reportedId,
                report.reportDate
            )
        }
    }
}
