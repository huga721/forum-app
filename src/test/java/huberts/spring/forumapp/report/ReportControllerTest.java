package huberts.spring.forumapp.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.report.ReportDoesntExistException;
import huberts.spring.forumapp.exception.report.ReportRealiseException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.jwt.JwtKey;
import huberts.spring.forumapp.report.dto.ReportReasonDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class ReportControllerTest extends ContainerIT {

    private static final String REPORT_REASON = "test reason of report";
    private static final String STRING_TOPIC = "Topic";
    private static final String STRING_COMMENT = "Comment";
    private static final String EMPTY_STRING = "";

    private static final String CREATE_TOPIC_REPORT_ENDPOINT = "/reports/topic/1";
    private static final String CREATE_TOPIC_REPORT_TOPIC_IS_CLOSED_ENDPOINT = "/reports/topic/8";
    private static final String CREATE_TOPIC_REPORT_DOESNT_EXIST_ENDPOINT = "/reports/topic/999";
    private static final String CREATE_COMMENT_REPORT_ENDPOINT = "/reports/comment/1";
    private static final String CREATE_COMMENT_REPORT_TOPIC_IS_CLOSED_ENDPOINT = "/reports/comment/5";
    private static final String CREATE_COMMENT_REPORT_DOESNT_EXIST_ENDPOINT = "/reports/comment/999";
    private static final String GET_ALL_REPORTS_ENDPOINT = "/reports";
    private static final String GET_REPORT_BY_ID_ENDPOINT = "/reports/1";
    private static final String GET_REPORT_BY_ID_DOESNT_EXIST_ENDPOINT = "/reports/999";
    private static final String GET_ALL_NOT_SEEN_REPORTS_ENDPOINT = "/reports/not-seen";
    private static final String MARK_REPORT_AS_SEEN_ENDPOINT = "/reports/mark-as-seen/4";
    private static final String MARK_REPORT_AS_SEEN_DOESNT_EXIST_ENDPOINT = "/reports/mark-as-seen/999";
    private static final String EXECUTE_REPORTS_OF_TOPIC_ENDPOINT = "/reports/execute/topic/14";
    private static final String EXECUTE_REPORTS_OF_TOPIC_DOESNT_EXIST_ENDPOINT = "/reports/execute/topic/999";
    private static final String EXECUTE_REPORTS_OF_TOPIC_TOPIC_IS_CLOSED_ENDPOINT = "/reports/execute/topic/8";
    private static final String EXECUTE_REPORTS_OF_TOPIC_NOT_ENOUGH_REPORTS_ENDPOINT = "/reports/execute/topic/1";
    private static final String EXECUTE_REPORTS_OF_COMMENT_ENDPOINT = "/reports/execute/comment/8";
    private static final String EXECUTE_REPORTS_OF_COMMENT_DOESNT_EXIST_ENDPOINT = "/reports/execute/comment/999";
    private static final String EXECUTE_REPORTS_OF_COMMENT_TOPIC_IS_CLOSED_ENDPOINT = "/reports/execute/comment/5";
    private static final String EXECUTE_REPORTS_OF_COMMENT_NOT_ENOUGH_REPORTS_ENDPOINT = "/reports/execute/comment/1";
    private static final String DELETE_REPORT_ENDPOINT = "/reports/delete/15";
    private static final String DELETE_REPORT_DOESNT_EXIST_ENDPOINT = "/reports/delete/999";

    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_TOKEN = "wrong_token_123";
    private static final String LOCATION = "location";
    private static final String NEW_REPORT_LOCATION = "/reports";

    private static final String REPORTED_OBJECT_JSON_PATH = "$.reportedObject";
    private static final String REASON_JSON_PATH = "$.reason";
    private static final String REASON_ARRAY_0_JSON_PATH = "$.[0].reason";
    private static final String REASON_ARRAY_1_JSON_PATH = "$.[1].reason";
    private static final String ID_JSON_PATH = "$.id";
    private static final String ID_ARRAY_0_JSON_PATH = "$.[0].id";
    private static final String ID_ARRAY_1_JSON_PATH = "$.[1].id";
    private static final String SEEN_JSON_PATH = "$.seen";
    private static final String SEEN_ARRAY_0_JSON_PATH = "$.[0].seen";
    private static final String SEEN_ARRAY_1_JSON_PATH = "$.[1].seen";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("post /reports/topic/{topicId} endpoint")
    @Nested
    class ReportsTopicTopicIdTests {

        @DisplayName("Should create report for topic, HTTP status 201")
        @Test
        void shouldCreateReportForTopic() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(REASON_JSON_PATH).value(REPORT_REASON))
                    .andExpect(jsonPath(REPORTED_OBJECT_JSON_PATH).value(STRING_TOPIC))
                    .andExpect(header().string(LOCATION, NEW_REPORT_LOCATION));
        }

        @DisplayName("Should throw TopicIsClosedException when topic to report is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicToReportIsClosed() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_REPORT_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_REPORT_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when reason field in request body is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenReasonFieldInRequestBodyIsEmpty() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(EMPTY_STRING);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not create report when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotCreateReport_WhenJWTIsWrong() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            mockMvc.perform(post(CREATE_TOPIC_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not create report when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateReport_WhenNoAuthorization() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            mockMvc.perform(post(CREATE_TOPIC_REPORT_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("post /reports/comment/{commentId} endpoint")
    @Nested
    class ReportsCommentCommentIdTests {

        @DisplayName("Should create report for comment, HTTP status 201")
        @Test
        void shouldCreateReportForComment() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(REASON_JSON_PATH).value(REPORT_REASON))
                    .andExpect(jsonPath(REPORTED_OBJECT_JSON_PATH).value(STRING_COMMENT))
                    .andExpect(header().string(LOCATION, NEW_REPORT_LOCATION));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to report is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToReportIsClosed() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_REPORT_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw CommentDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenTopicDoesntExist() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_REPORT_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));

        }

        @DisplayName("Should throw MethodArgumentNotValidException when reason field in request body is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenReasonFieldInRequestBodyIsEmpty() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(EMPTY_STRING);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        }

        @DisplayName("Should not create report when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotCreateReport_WhenJWTIsWrong() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            mockMvc.perform(post(CREATE_COMMENT_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not create report when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateReport_WhenNoAuthorization() throws Exception {
            ReportReasonDTO reportReason = new ReportReasonDTO(REPORT_REASON);
            String reportReasonJson = objectMapper.writeValueAsString(reportReason);
            mockMvc.perform(post(CREATE_COMMENT_REPORT_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reportReasonJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("get /reports endpoint")
    @Nested
    class ReportsTests {

        @DisplayName("Should return all reports, HTTP status 200")
        @Test
        void shouldReturnAllReports() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_ALL_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(REASON_ARRAY_0_JSON_PATH).value(REPORT_REASON))
                    .andExpect(jsonPath(REASON_ARRAY_1_JSON_PATH).value(REPORT_REASON));
        }

        @DisplayName("Should not return all reports when requested by user, HTTP status 403")
        @Test
        void shouldNotReturnAllReports_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_ALL_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not return all reports when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnAllReports_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(get(GET_ALL_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not return all reports when no authorization, HTTP status 401")
        @Test
        void shouldNotReturnAllReports_WhenNoAuthorization() throws Exception {
            mockMvc.perform(get(GET_ALL_REPORTS_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("get /reports/{reportId} endpoint")
    @Nested
    class ReportsReportIdTests {

        @DisplayName("Should return report, HTTP status 200")
        @Test
        void shouldReturnReport() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_REPORT_BY_ID_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_JSON_PATH).value(1))
                    .andExpect(jsonPath(REASON_JSON_PATH).value(REPORT_REASON));
        }

        @DisplayName("Should throw ReportDoesntExistException when report doesn't exist, HTTP status 404")
        @Test
        void shouldThrowReportDoesntExistException_WhenReportDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_REPORT_BY_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ReportDoesntExistException));
        }

        @DisplayName("Should not return report when requested by user, HTTP status 403")
        @Test
        void shouldNotReturnReport_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_REPORT_BY_ID_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));

        }

        @DisplayName("Should not return report when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnReport_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(get(GET_REPORT_BY_ID_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not return report when no authorization, HTTP status 401")
        @Test
        void shouldNotReturnReport_WhenNoAuthorization() throws Exception {
            mockMvc.perform(get(GET_REPORT_BY_ID_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("get /reports/not-seen endpoint")
    @Nested
    class ReportsNotSeenTests {

        @DisplayName("Should return all not seen reports, HTTP status 200")
        @Test
        void shouldReturnAllNotSeenReports() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_ALL_NOT_SEEN_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_ARRAY_0_JSON_PATH).value(2))
                    .andExpect(jsonPath(ID_ARRAY_1_JSON_PATH).value(3))
                    .andExpect(jsonPath(SEEN_ARRAY_0_JSON_PATH).value(false))
                    .andExpect(jsonPath(SEEN_ARRAY_1_JSON_PATH).value(false));
        }

        @DisplayName("Should not return all not seen reports when requested by user, HTTP status 403")
        @Test
        void shouldNotReturnAllNotSeenReports_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_ALL_NOT_SEEN_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not return all not seen reports when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnAllNotSeenReports_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(get(GET_ALL_NOT_SEEN_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not return all not seen reports when no authorization, HTTP status 401")
        @Test
        void shouldNotReturnAllNotSeenReports_WhenNoAuthorization() throws Exception {
            mockMvc.perform(get(GET_ALL_NOT_SEEN_REPORTS_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("put /reports/mark-as-seen/{reportId} endpoint")
    @Nested
    class ReportsMarkAsSeenReportIdTests {

        @DisplayName("Should change report to seen, HTTP status 200")
        @Test
        void shouldChangeReportToSeen() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(put(MARK_REPORT_AS_SEEN_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_JSON_PATH).value(4))
                    .andExpect(jsonPath(SEEN_JSON_PATH).value(true));
        }

        @DisplayName("Should throw ReportDoesntExistException when report doesn't exist, HTTP status 404")
        @Test
        void shouldThrowReportDoesntExistException_WhenReportDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(put(MARK_REPORT_AS_SEEN_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ReportDoesntExistException));
        }

        @DisplayName("Should not change report to seen when requested by user, HTTP status 403")
        @Test
        void shouldNotChangeReportToSeen_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(put(MARK_REPORT_AS_SEEN_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not change report to seen when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotChangeReportToSeen_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(put(MARK_REPORT_AS_SEEN_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not change report to seen when no authorization, HTTP status 401")
        @Test
        void shouldNotChangeReportToSeen_WhenNoAuthorization() throws Exception {
            mockMvc.perform(put(MARK_REPORT_AS_SEEN_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /reports/execute/topic/{topicId} endpoint")
    @Nested
    class ReportsExecuteTopicTopicIdTests {

        @DisplayName("Should delete topic and warn author due to more than 5 reports, HTTP status 204")
        @Test
        void shouldDeleteTopicAndWarnAuthorDueToMoreThan5Reports() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw TopicIsClosedException when executing topic is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenExecutingTopicIsClosed() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_TOPIC_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw ReportRealiseException when executing topic have less than 5 reports, HTTP status 400")
        @Test
        void shouldThrowReportRealiseException_WhenExecutingTopicHaveLessThan5Reports() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_TOPIC_NOT_ENOUGH_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ReportRealiseException));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_TOPIC_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should not execute reports of topic when requested by user, HTTP status 403")
        @Test
        void shouldNotExecuteReportsOfTopic_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not execute reports of topic when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotExecuteReportsOfTopic_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not execute reports of topic when no authorization, HTTP status 401")
        @Test
        void shouldNotExecuteReportsOfTopic_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_TOPIC_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /reports/execute/comment/{commentId} endpoint")
    @Nested
    class ReportsExecuteCommentCommentIdTests {

        @DisplayName("Should delete comment and warn author due to more than 5 reports, HTTP status 204")
        @Test
        void shouldDeleteCommentAndWarnAuthorDueToMoreThan5Reports() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_COMMENT_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_COMMENT_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to execute reports is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToExecuteReportsIsClosed() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_COMMENT_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw ReportRealiseException when executing comment have less than 5 reports, HTTP status 400")
        @Test
        void shouldThrowReportRealiseException_WhenExecutingCommentHaveLessThan5Reports() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_COMMENT_NOT_ENOUGH_REPORTS_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ReportRealiseException));
        }

        @DisplayName("Should not execute reports of comment when requested by user, HTTP status 403")
        @Test
        void shouldNotExecuteReportsOfComment_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_COMMENT_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not execute reports of comment when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotExecuteReportsOfComment_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_COMMENT_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not execute reports of comment when no authorization, HTTP status 401")
        @Test
        void shouldNotExecuteReportsOfComment_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(EXECUTE_REPORTS_OF_COMMENT_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /reports/delete/{reportId} endpoint")
    @Nested
    class ReportsDeleteReportIdTests {

        @DisplayName("Should delete report, HTTP status 204")
        @Test
        void shouldDeleteReport() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw ReportDoesntExistException when report doesn't exist, HTTP status 404")
        @Test
        void shouldThrowReportDoesntExistException_WhenReportDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_REPORT_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ReportDoesntExistException));
        }

        @DisplayName("Should not delete report when requested by user, HTTP status 403")
        @Test
        void shouldNotDeleteReport_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not delete report when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteReport_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_REPORT_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete report when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteReport_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_REPORT_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }
}