package huberts.spring.forumapp.report;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.comment.CommentRepository;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.report.ReportDoesntExistException;
import huberts.spring.forumapp.exception.report.ReportRealiseException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.report.dto.ReportDTO;
import huberts.spring.forumapp.report.dto.ReportReasonDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicRepository;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.common.UtilityService;
import huberts.spring.forumapp.warning.WarningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService implements ReportServiceApi {

    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final WarningService warningService;
    private final UtilityService utilityService;

    private static final String COMMENT_DOESNT_EXIST_EXCEPTION = "Comment with id \"%d\" doesn't exist.";
    private static final String TOPIC_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private static final String TOPIC_IS_CLOSED_EXCEPTION = "Topic with id \"%d\" is closed.";
    private static final String REPORT_REALISE_EXIST_EXCEPTION = "Can't realise reports because there are less than 5 reports.";
    private static final String REPORT_DOESNT_EXIST_EXCEPTION = "Report with id \"%d\" doesn't exist.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    @Override
    public ReportDTO createCommentReport(Long commentId, ReportReasonDTO reportReasonDTO, String username) {
        log.info("Creating a report for comment with id {}", commentId);
        User user = findUserByUsername(username);
        Comment commentFound = findCommentById(commentId);

        validateTopicClosed(commentFound.getTopic());
        utilityService.updateUserLastActivity(username);
        log.info("Report created");
        return buildAndSaveCommentReport(user, reportReasonDTO, commentFound);
    }

    private void validateTopicClosed(Topic topic) {
        if (topic.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, topic.getId());
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
    }

    private ReportDTO buildAndSaveCommentReport(User user, ReportReasonDTO reportReasonDTO, Comment comment) {
        Report reportBuilt = ReportMapper.buildReport(user, reportReasonDTO.reason(), comment);
        reportRepository.save(reportBuilt);
        return ReportMapper.buildReportTopicDTO(reportBuilt);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    private Comment findCommentById(Long commentId) {
        log.info("Finding comment with id {}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, commentId);
                    log.error(EXCEPTION_OCCURRED, new CommentDoesntExistException(errorMessage));
                    throw new CommentDoesntExistException(errorMessage);
                });
    }

    @Override
    public ReportDTO createTopicReport(Long topicId, ReportReasonDTO reportReasonDTO, String username) {
        log.info("Creating a report for topic with id {}", topicId);
        User user = findUserByUsername(username);
        Topic topicFound = findTopicById(topicId);

        validateTopicClosed(topicFound);
        utilityService.updateUserLastActivity(username);
        log.info("Report created");
        return buildAndSaveTopicReport(user, reportReasonDTO, topicFound);
    }

    private ReportDTO buildAndSaveTopicReport(User user, ReportReasonDTO reportReasonDTO, Topic topic) {
        Report reportBuilt = ReportMapper.buildReport(user, reportReasonDTO.reason(), topic);
        reportRepository.save(reportBuilt);
        return ReportMapper.buildReportTopicDTO(reportBuilt);
    }

    private Topic findTopicById(Long topicId) {
        log.info("Finding topic with id {}", topicId);
        return topicRepository.findById(topicId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_DOESNT_EXIST_EXCEPTION, topicId);
                    log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
                    throw new TopicDoesntExistException(errorMessage);
                });
    }

    @Override
    public ReportDTO getReportById(Long reportId) {
        log.info("Getting a report with id {}", reportId);
        return ReportMapper.buildReportTopicDTO(findReportById(reportId));
    }

    private Report findReportById(Long reportId) {
        log.info("Finding report with id {}", reportId);
        return reportRepository.findById(reportId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(REPORT_DOESNT_EXIST_EXCEPTION, reportId);
                    log.error(EXCEPTION_OCCURRED, new ReportDoesntExistException(errorMessage));
                    throw new ReportDoesntExistException(errorMessage);
                });
    }

    @Override
    public List<ReportDTO> getAllReports() {
        log.info("Getting all reports");
        return ReportMapper.mapReportsToDTOList(reportRepository.findAll());
    }

    @Override
    public List<ReportDTO> getAllNotSeenReports() {
        log.info("Getting all not seen reports");
        return ReportMapper.mapReportsToNotSeenDTOList(reportRepository.findAll());
    }

    @Override
    public ReportDTO markReportAsSeen(Long reportId) {
        log.info("Setting a report with id {} as seen", reportId);
        Report reportFound = findReportById(reportId);
        reportFound.setSeen(true);
        log.info("Report set");
        return ReportMapper.buildReportTopicDTO(reportFound);
    }

    @Override
    public void executeReportAndWarnTopicAuthor(Long topicId) {
        log.info("Deleting all reports of topic and deleting topic with id {} and warning author of topic", topicId);
        Topic topicFound = findTopicById(topicId);
        String author = topicFound.getUser().getUsername();

        validateTopicClosed(topicFound);
        validateReports(topicFound.getReports());

        topicRepository.delete(topicFound);
        warningService.giveWarning(author);
        log.info("Deleted all reports");
    }

    private void validateReports(List<Report> reports) {
        if (reports.size() < 5) {
            log.error(EXCEPTION_OCCURRED, new ReportRealiseException(REPORT_REALISE_EXIST_EXCEPTION));
            throw new ReportRealiseException(REPORT_REALISE_EXIST_EXCEPTION);
        }
    }

    @Override
    public void executeReportAndWarnCommentAuthor(Long commentId) {
        log.info("Deleting all reports for comment with id {} and warning author of comment", commentId);
        Comment commentFound = findCommentById(commentId);
        String author = commentFound.getUser().getUsername();

        validateTopicClosed(commentFound.getTopic());
        validateReports(commentFound.getReports());

        commentRepository.delete(commentFound);
        warningService.giveWarning(author);
        log.info("Deleted all reports");
    }

    @Override
    public void deleteReport(Long reportId) {
        log.info("Deleting report with id {}", reportId);
        Report reportFound = findReportById(reportId);
        reportRepository.delete(reportFound);
        log.info("Report deleted");
    }
}