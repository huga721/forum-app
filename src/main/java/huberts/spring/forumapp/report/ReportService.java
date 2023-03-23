package huberts.spring.forumapp.report;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.comment.CommentRepository;
import huberts.spring.forumapp.exception.comment.CommentExistException;
import huberts.spring.forumapp.exception.report.ReportExistException;
import huberts.spring.forumapp.exception.report.ReportRealiseException;
import huberts.spring.forumapp.exception.topic.TopicExistException;
import huberts.spring.forumapp.report.dto.ReportDTO;
import huberts.spring.forumapp.report.dto.ReportReasonDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicRepository;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.warning.WarningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService implements ReportServiceApi {

    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final WarningService warningService;

    private static final String COMMENT_DOESNT_EXIST_EXCEPTION = "Comment with id \"%d\" doesn't exist.";
    private static final String TOPIC_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private static final String REPORT_DOESNT_EXIST_EXCEPTION = "Report with id \"%d\" doesn't exist.";
    private static final String REPORT_REALISE_EXIST_EXCEPTION = "Can't realise reports because there are less than 5 reports.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    @Override
    public ReportDTO createCommentReport(Long id, ReportReasonDTO reasonDTO, String username) {
        log.info("Creating a report for comment with id {}", id);
        User userReporting = userRepository.findByUsername(username).get();
        Comment commentFound = findCommentById(id);

        String reason = reasonDTO.getReason();

        Report reportBuilt = ReportMapper.buildReport(userReporting, reason, commentFound);
        Report reportSaved = reportRepository.save(reportBuilt);
        log.info("Report created");
        return ReportMapper.buildReportTopicDTO(reportSaved);
    }

    private Comment findCommentById(Long id) {
        log.info("Finding comment with id {}", id);
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new CommentExistException(errorMessage));
                    return new CommentExistException(errorMessage);
                });
    }

    @Override
    public ReportDTO createTopicReport(Long id, ReportReasonDTO reasonDTO, String username) {
        log.info("Creating a report for topic with id {}", id);
        User userReporting = userRepository.findByUsername(username).get();
        Topic topicFound = findTopicById(id);

        String reason = reasonDTO.getReason();

        Report reportBuilt = ReportMapper.buildReport(userReporting, reason, topicFound);
        Report reportSaved = reportRepository.save(reportBuilt);

        log.info("Report created");
        return ReportMapper.buildReportTopicDTO(reportSaved);
    }

    private Topic findTopicById(Long id) {
        log.info("Finding topic with id {}", id);
        return topicRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new TopicExistException(errorMessage));
                    return new TopicExistException(errorMessage);
                });
    }

    @Override
    public ReportDTO getReportById(Long id) {
        log.info("Getting a report with id {}", id);
        return ReportMapper.buildReportTopicDTO(findReport(id));
    }

    private Report findReport(Long id) {
        log.info("Finding report with id {}", id);
        return reportRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(REPORT_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new ReportExistException(errorMessage));
                    return new ReportExistException(errorMessage);
                });
    }

    @Override
    public List<ReportDTO> getAllReports() {
        log.info("Getting all reports");
        return ReportMapper.mapReportToDTO(reportRepository.findAll());
    }

    @Override
    public List<ReportDTO> getAllNotSeenReports() {
        log.info("Getting all not seen reports");
        return ReportMapper.mapReportToDTO(reportRepository.findAll()).stream()
                .filter(reportDTO -> !reportDTO.isSeen())
                .collect(Collectors.toList());
    }

    @Override
    public ReportDTO markReportAsSeen(Long id) {
        log.info("Setting a report with id {} as seen", id);
        Report foundReport = findReport(id);
        foundReport.setSeen(true);
        log.info("Report set");
        return ReportMapper.buildReportTopicDTO(foundReport);
    }

    @Override
    public void executeReportAndWarnTopicAuthor(Long id) {
        log.info("Deleting all reports for topic with id {} and warning author of topic", id);
        Topic topicFound = findTopicById(id);

        if (topicFound.getReports().size() < 5) {
            log.error(REPORT_REALISE_EXIST_EXCEPTION);
            throw new ReportRealiseException(REPORT_REALISE_EXIST_EXCEPTION);
        }

        String author = topicFound.getUsers().get(0).getUsername();

        topicRepository.delete(topicFound);
        warningService.giveWarning(author);
        log.info("Deleted all reports");
    }

    @Override
    public void executeReportAndWarnCommentAuthor(Long id) {
        log.info("Deleting all reports for comment with id {} and warning author of comment", id);
        Comment commentFound = findCommentById(id);

        if (commentFound.getReports().size() < 5) {
            log.error(REPORT_REALISE_EXIST_EXCEPTION);
            throw new ReportRealiseException(REPORT_REALISE_EXIST_EXCEPTION);
        }

        String author = commentFound.getUser().getUsername();

        commentRepository.delete(commentFound);
        warningService.giveWarning(author);
        log.info("Deleted all reports");
    }

    @Override
    public void deleteReport(Long id) {
        log.info("Deleting report with id {}", id);
        Report reportFound = findReport(id);
        reportRepository.delete(reportFound);
        log.info("Report deleted");
    }
}