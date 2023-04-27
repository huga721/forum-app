package huberts.spring.forumapp.report;

import huberts.spring.forumapp.report.dto.ReportDTO;
import huberts.spring.forumapp.report.dto.ReportReasonDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService service;

    @UserRole
    @PostMapping("/topic/{topicId}")
    ResponseEntity<ReportDTO> saveNewTopicReport(@PathVariable Long topicId, @RequestBody @Valid ReportReasonDTO reportReasonDTO,
                                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        ReportDTO reportCreated = service.createTopicReport(topicId, reportReasonDTO, username);
        return ResponseEntity.created(URI.create("/reports")).body(reportCreated);
    }

    @UserRole
    @PostMapping("/comment/{commentId}")
    ResponseEntity<ReportDTO> saveNewCommentReport(@PathVariable Long commentId, @RequestBody @Valid ReportReasonDTO reportReasonDTO,
                                                   Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        ReportDTO reportCreated = service.createCommentReport(commentId, reportReasonDTO, username);
        return ResponseEntity.created(URI.create("/reports")).body(reportCreated);
    }

    @ModeratorRole
    @GetMapping()
    ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = service.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @ModeratorRole
    @GetMapping("/{reportId}")
    ResponseEntity<ReportDTO> getReportById(@PathVariable Long reportId) {
        ReportDTO report = service.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    @ModeratorRole
    @GetMapping("/not-seen")
    ResponseEntity<List<ReportDTO>> getAllNotSeenReports() {
        List<ReportDTO> reports = service.getAllNotSeenReports();
        return ResponseEntity.ok(reports);
    }

    @ModeratorRole
    @PutMapping("/mark-as-seen/{reportId}")
    ResponseEntity<ReportDTO> updateReportAsSeen(@PathVariable Long reportId) {
        ReportDTO report = service.markReportAsSeen(reportId);
        return ResponseEntity.ok(report);
    }

    @ModeratorRole
    @DeleteMapping("/execute/topic/{topicId}")
    ResponseEntity<Void> executeTopicReports(@PathVariable Long topicId) {
        service.executeReportAndWarnTopicAuthor(topicId);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @DeleteMapping("/execute/comment/{commentId}")
    ResponseEntity<Void> executeCommentReports(@PathVariable Long commentId) {
        service.executeReportAndWarnCommentAuthor(commentId);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @DeleteMapping("/delete/{reportId}")
    ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        service.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}