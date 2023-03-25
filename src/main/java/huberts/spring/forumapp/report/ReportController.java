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
    @PostMapping("/topic/{id}")
    ResponseEntity<ReportDTO> saveNewTopicReport(@PathVariable Long id, @RequestBody @Valid ReportReasonDTO reportReasonDTO,
                                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        ReportDTO reportCreated = service.createTopicReport(id, reportReasonDTO, username);
        return ResponseEntity.created(URI.create("/reports")).body(reportCreated);
    }

    @UserRole
    @PostMapping("/comment/{id}")
    ResponseEntity<ReportDTO> saveNewCommentReport(@PathVariable Long id, @RequestBody @Valid ReportReasonDTO reportReasonDTO,
                                                   Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        ReportDTO reportCreated = service.createCommentReport(id, reportReasonDTO, username);
        return ResponseEntity.created(URI.create("/reports")).body(reportCreated);
    }

    @ModeratorRole
    @GetMapping()
    ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = service.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @ModeratorRole
    @GetMapping("/{id}")
    ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        ReportDTO report = service.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @ModeratorRole
    @GetMapping("/not-seen")
    ResponseEntity<List<ReportDTO>> getAllNotSeenReports() {
        List<ReportDTO> reports = service.getAllNotSeenReports();
        return ResponseEntity.ok(reports);
    }

    @ModeratorRole
    @PutMapping("/mark-as-seen/{id}")
    ResponseEntity<ReportDTO> updateReportAsSeen(@PathVariable Long id) {
        ReportDTO report = service.markReportAsSeen(id);
        return ResponseEntity.ok(report);
    }

    @ModeratorRole
    @DeleteMapping("/execute/topic/{id}")
    ResponseEntity<Void> executeTopicReports(@PathVariable Long id) {
        service.executeReportAndWarnTopicAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @DeleteMapping("/execute/comment/{id}")
    ResponseEntity<Void> executeCommentReports(@PathVariable Long id) {
        service.executeReportAndWarnCommentAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        service.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}