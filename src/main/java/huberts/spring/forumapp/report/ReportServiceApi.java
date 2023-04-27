package huberts.spring.forumapp.report;

import huberts.spring.forumapp.report.dto.ReportDTO;
import huberts.spring.forumapp.report.dto.ReportReasonDTO;

import java.util.List;

public interface ReportServiceApi {
    ReportDTO createCommentReport(Long commentId, ReportReasonDTO reportReasonDTO, String username);
    ReportDTO createTopicReport(Long topicId, ReportReasonDTO reportReasonDTO, String username);

    ReportDTO getReportById(Long reportId);
    List<ReportDTO> getAllReports();
    List<ReportDTO> getAllNotSeenReports();

    ReportDTO markReportAsSeen(Long reportId);

    void executeReportAndWarnTopicAuthor(Long topicId);
    void executeReportAndWarnCommentAuthor(Long commentId);
    void deleteReport(Long reportId);
}