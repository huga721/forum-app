package huberts.spring.forumapp.report;

import huberts.spring.forumapp.report.dto.ReportDTO;
import huberts.spring.forumapp.report.dto.ReportReasonDTO;

import java.util.List;

public interface ReportServiceApi {
    ReportDTO createCommentReport(Long id, ReportReasonDTO reasonDTO, String username);
    ReportDTO createTopicReport(Long id, ReportReasonDTO reasonDTO, String username);

    ReportDTO getReportById(Long id);
    List<ReportDTO> getAllReports();
    List<ReportDTO> getAllNotSeenReports();

    ReportDTO markReportAsSeen(Long id);

    void executeReportAndWarnTopicAuthor(Long id);
    void executeReportAndWarnCommentAuthor(Long id);
    void deleteReport(Long id);
}