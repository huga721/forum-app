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
import huberts.spring.forumapp.warning.WarningRepository;
import huberts.spring.forumapp.warning.WarningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    private final static String USERNAME = "user";
    private final static String TITLE = "title of topic";
    private final static String CONTENT = "information about topic";
    private static final String REPORT_REASON = "test reason of report";
    private final static String STRING_TOPIC = "Topic";
    private final static String STRING_COMMENT = "Comment";

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WarningService warningService;
    @Mock
    private WarningRepository warningRepository;
    @InjectMocks
    private ReportService service;

    private User user;
    private Comment comment;
    private Topic topic;
    private Report report;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username(USERNAME)
                .warnings(List.of())
                .build();
        topic = Topic.builder()
                .id(1L)
                .title(TITLE)
                .user(user)
                .content(CONTENT)
                .likes(List.of())
                .closed(false)
                .comments(List.of())
                .build();
        comment = Comment.builder()
                .id(1L)
                .content(CONTENT)
                .likes(List.of())
                .topic(topic)
                .user(user).build();
        report = Report.builder()
                .reason(REPORT_REASON)
                .id(2L)
                .topic(topic)
                .user(user)
                .build();
    }

    @DisplayName("createCommentReport method")
    @Nested
    class CreateCommentReportTests {

        @DisplayName("Should create report for comment")
        @Test
        void shouldCreateReportForComment() {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

            ReportDTO reportCreated = service.createCommentReport(1L, reportReason, USERNAME);
            assertEquals(reportCreated.reason(), REPORT_REASON);
            assertEquals(reportCreated.reportedObject(), STRING_COMMENT);
            assertEquals(reportCreated.whoReported(), USERNAME);
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to report is closed")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToReportIsClosed() {
            topic.setClosed(true);
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

            assertThrows(TopicIsClosedException.class, () -> service.createCommentReport(1L, reportReason, USERNAME));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment with given id doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentWithGivenIdDoesntExist() {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(CommentDoesntExistException.class, () -> service.createCommentReport(1L, reportReason, USERNAME));
        }
    }

    @DisplayName("createTopicReport method")
    @Nested
    class CreateTopicReportTests {

        @DisplayName("Should create report for topic")
        @Test
        void shouldCreateReportForTopic() {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            ReportDTO reportCreated = service.createTopicReport(1L, reportReason, USERNAME);

            assertEquals(reportCreated.reason(), REPORT_REASON);
            assertEquals(reportCreated.reportedObject(), STRING_TOPIC);
            assertEquals(reportCreated.whoReported(), USERNAME);
        }

        @DisplayName("Should throw TopicIsClosedException when topic to report is closed")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicToReportIsClosed() {
            topic.setClosed(true);
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));

            assertThrows(TopicIsClosedException.class, () -> service.createTopicReport(1L, reportReason, USERNAME));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(TopicDoesntExistException.class, () -> service.createTopicReport(1L, reportReason, USERNAME));

        }
    }

    @DisplayName("getReportById method")
    @Nested
    class GetReportByIdTests {

        @DisplayName("Should get report")
        @Test
        void shouldGetReport() {
            when(reportRepository.findById(any(Long.class))).thenReturn(Optional.of(report));
            ReportDTO report = service.getReportById(1L);
            assertEquals(report.whoReported(), USERNAME);
            assertEquals(report.reason(), REPORT_REASON);
        }

        @DisplayName("Should throw ReportDoesntExistException when report with given id doesn't exist")
        @Test
        void shouldThrowReportDoesntExistException_WhenReportWithGivenIdDoesntExist() {
            assertThrows(ReportDoesntExistException.class, () -> service.getReportById(1L));
        }
    }

    @DisplayName("getAllReports method")
    @Nested
    class GetAllReportsTests {

        @DisplayName("Should return all reports")
        @Test
        void shouldReturnAllReports() {
            when(reportRepository.findAll()).thenReturn(List.of(report, report));
            List<ReportDTO> reports = service.getAllReports();

            assertEquals(reports.size(), 2);
            assertEquals(reports.get(0).whoReported(), USERNAME);
        }

        @DisplayName("Should return empty list")
        @Test
        void shouldReturnEmptyList() {
            when(reportRepository.findAll()).thenReturn(List.of());
            List<ReportDTO> reports = service.getAllReports();
            assertTrue(reports.isEmpty());
        }
    }

    @DisplayName("getAllNotSeenReports method")
    @Nested
    class GetAllNotSeenReportsTests {

        @DisplayName("Should return all not seen reports")
        @Test
        void shouldReturnAllNotSeenReports() {
            Report seenReport = Report.builder()
                    .seen(true)
                    .build();

            when(reportRepository.findAll()).thenReturn(List.of(seenReport, report, seenReport));
            List<ReportDTO> reports = service.getAllNotSeenReports();

            assertEquals(reports.size(), 1);
            assertFalse(reports.get(0).seen());
        }

        @DisplayName("Should return empty list when every report is seen")
        @Test
        void shouldReturnEmptyList_WhenEveryReportIsSeen() {
            Report seenReport = Report.builder()
                    .seen(true)
                    .build();
            when(reportRepository.findAll()).thenReturn(List.of(seenReport, seenReport, seenReport));
            List<ReportDTO> reports = service.getAllNotSeenReports();
            assertTrue(reports.isEmpty());
        }
    }

    @DisplayName("markReportAsSeen method")
    @Nested
    class MarkReportAsSeenTests {

        @DisplayName("Should set field seen to true")
        @Test
        void shouldSetFieldSeenToTrue() {
            report.setSeen(false);
            when(reportRepository.findById(any(Long.class))).thenReturn(Optional.of(report));
            ReportDTO report = service.updateReportAsSeen(1L);
            assertTrue(report.seen());
        }

        @DisplayName("Should do nothing when report field seen is already true")
        @Test
        void shouldDoNothing_WhenReportFieldSeenIsAlreadyTrue() {
            report.setSeen(true);
            when(reportRepository.findById(any(Long.class))).thenReturn(Optional.of(report));
            ReportDTO report = service.updateReportAsSeen(1L);
            assertTrue(report.seen());
        }

        @DisplayName("Should throw ReportDoesntExistException when report with given id doesn't exist")
        @Test
        void shouldThrowReportDoesntExistException_WhenReportWithGivenIdDoesntExist() {
            assertThrows(ReportDoesntExistException.class, () -> service.updateReportAsSeen(1L));
        }
    }

    @DisplayName("executeReportAndWarnTopicAuthor method")
    @Nested
    class ExecuteReportAndWarnTopicAuthorTests {

        @DisplayName("Should delete topic and warn user when topic have more than 5 reports")
        @Test
        void shouldDeleteTopicThatAndWarnUser_WhenTopicHaveMoreThan5Reports() {
            topic.setReports(List.of(report, report, report, report, report));

            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            service.executeReportAndWarnTopicAuthor(1L);

            verify(topicRepository, times(1)).delete(topic);
            verify(warningService, times(1)).createWarning(USERNAME);
        }

        @DisplayName("Should throw ReportRealiseException when topic has less than 5 reports")
        @Test
        void shouldThrowReportRealiseException_WhenTopicHasLessThan5Reports() {
            topic.setReports(List.of(report));

            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));

            assertThrows(ReportRealiseException.class, () -> service.executeReportAndWarnTopicAuthor(1L));
        }

        @DisplayName("Should throw TopicIsClosedException when topic to execute reports is closed")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicToExecuteReportsIsClosed() {
            topic.setClosed(true);
            topic.setReports(List.of(report, report, report, report, report));

            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));

            assertThrows(TopicIsClosedException.class, () -> service.executeReportAndWarnTopicAuthor(1L));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() {
            assertThrows(TopicDoesntExistException.class, () -> service.executeReportAndWarnTopicAuthor(1L));
        }
    }

    @DisplayName("executeReportAndWarnCommentAuthor method")
    @Nested
    class ExecuteReportAndWarnCommentAuthorTests {

        @DisplayName("Should delete comment and warn user when topic of comment have more than 5 reports")
        @Test
        void shouldDeleteCommentAndWarnUser_WhenTopicOfCommentHaveModeThan5Reports() {
            comment.setReports(List.of(report, report, report, report, report));

            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            service.executeReportAndWarnCommentAuthor(1L);

            verify(commentRepository, times(1)).delete(comment);
            verify(warningService, times(1)).createWarning(USERNAME);
        }

        @DisplayName("Should throw ReportRealiseException when comment has less than 5 reports")
        @Test
        void shouldThrowReportRealiseException_WhenCommentHasLessThan5Reports() {
            comment.setReports(List.of(report));
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            assertThrows(ReportRealiseException.class, () -> service.executeReportAndWarnCommentAuthor(1L));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to execute reports is closed")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToExecuteReportsIsClosed() {
            topic.setClosed(true);
            topic.setReports(List.of(report, report, report, report, report));

            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

            assertThrows(TopicIsClosedException.class, () -> service.executeReportAndWarnCommentAuthor(1L));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment with given id doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentWithGivenIdDoesntExist() {
            assertThrows(CommentDoesntExistException.class, () -> service.executeReportAndWarnCommentAuthor(1L));
        }
    }

    @DisplayName("deleteReport method")
    @Nested
    class DeleteReportTests {

        @DisplayName("Should delete report")
        @Test
        void shouldDeleteReport() {
            when(reportRepository.findById(any(Long.class))).thenReturn(Optional.of(report));
            service.deleteReport(1L);
            verify(reportRepository, times(1)).delete(report);
        }

        @DisplayName("Should throw ReportDoesntExistException when report with given id doesn't exist")
        @Test
        void shouldThrowReportDoesntExistException_WhenReportWithGivenIdDoesntExist() {
            assertThrows(ReportDoesntExistException.class, () -> service.deleteReport(1L));
        }
    }
}