package huberts.spring.forumapp.report;

import huberts.spring.forumapp.report.dto.ReportDTO;
import huberts.spring.forumapp.report.dto.ReportReasonDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/v1/reports")
public class ReportController {

    private final ReportService service;

    @UserRole
    @Operation(summary = "[USER] Create report of topic")
    @PostMapping("/topic/{topicId}")
    ResponseEntity<ReportDTO> createTopicReport(@PathVariable Long topicId, @RequestBody @Valid ReportReasonDTO reportReasonDTO,
                                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        ReportDTO reportCreated = service.createTopicReport(topicId, reportReasonDTO, username);
        return ResponseEntity.created(URI.create("/reports")).body(reportCreated);
    }

    @UserRole
    @Operation(summary = "[USER] Create report of comment")
    @PostMapping("/comment/{commentId}")
    ResponseEntity<ReportDTO> createCommentReport(@PathVariable Long commentId, @RequestBody @Valid ReportReasonDTO reportReasonDTO,
                                                   Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        ReportDTO reportCreated = service.createCommentReport(commentId, reportReasonDTO, username);
        return ResponseEntity.created(URI.create("/reports")).body(reportCreated);
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Get all reports")
    @GetMapping()
    List<ReportDTO> getAllReports() {
        List<ReportDTO> reports = service.getAllReports();
        return reports;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Get report by id")
    @GetMapping("/{reportId}")
    ReportDTO getReportById(@PathVariable Long reportId) {
        ReportDTO report = service.getReportById(reportId);
        return report;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Get all not seen reports")
    @GetMapping("/not-seen")
    List<ReportDTO> getAllNotSeenReports() {
        List<ReportDTO> reports = service.getAllNotSeenReports();
        return reports;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Update report as seen")
    @PutMapping("/mark-as-seen/{reportId}")
    ReportDTO updateReportAsSeen(@PathVariable Long reportId) {
        ReportDTO report = service.updateReportAsSeen(reportId);
        return report;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Execute reports and warn author of topic")
    @DeleteMapping("/execute/topic/{topicId}")
    ResponseEntity<Void> executeReportAndWarnTopicAuthor(@PathVariable Long topicId) {
        service.executeReportAndWarnTopicAuthor(topicId);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Execute reports and warn author of comment")
    @DeleteMapping("/execute/comment/{commentId}")
    ResponseEntity<Void> executeReportAndWarnCommentAuthor(@PathVariable Long commentId) {
        service.executeReportAndWarnCommentAuthor(commentId);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Delete report by report id")
    @DeleteMapping("/delete/{reportId}")
    ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        service.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}