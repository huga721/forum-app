package huberts.spring.forumapp.utility;

import huberts.spring.forumapp.exception.report.ReportDoesntExistException;
import huberts.spring.forumapp.exception.report.ReportRealiseException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.report.Report;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UtilityService {

    private final UserRepository userRepository;

    private static final String TOPIC_IS_CLOSED_EXCEPTION = "Topic with id \"%d\" is closed.";
    private static final String REPORT_REALISE_EXIST_EXCEPTION = "Can't realise reports because there are less than 5 reports.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    public void validateReports(List<Report> reports) {
        if (reports.size() < 5) {
            log.error(EXCEPTION_OCCURRED, new ReportDoesntExistException(REPORT_REALISE_EXIST_EXCEPTION));
            throw new ReportRealiseException(REPORT_REALISE_EXIST_EXCEPTION);
        }
    }

    public void validateTopicClosed(Topic topic) {
        if (topic.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, topic.getId());
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
    }

    public void updateUserLastActivity(String username) {
        userRepository.findByUsername(username).get()
                .setLastActivity(LocalDateTime.now());
    }
}