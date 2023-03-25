package huberts.spring.forumapp.report;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.report.dto.ReportDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportMapper {

    public static Report buildReport(User user, String reason, Comment comment) {
        return Report.builder()
                .user(user)
                .reason(reason)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Report buildReport(User user, String reason, Topic topic) {
        return Report.builder()
                .user(user)
                .reason(reason)
                .topic(topic)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ReportDTO buildReportTopicDTO(Report report) {
        return ReportDTO.builder()
                .id(report.getId())
                .seen(report.isSeen())
                .reason(report.getReason())
                .whoReported(report.getUser().getUsername())
                .reportedObject(getReportedObject(report))
                .objectId(getReportedObjectId(report))
                .build();
    }

    private static String getReportedObject(Report report) {
        if (report.getTopic() == null)
            return "Comment";
        else
            return "Topic";
    }

    private static Long getReportedObjectId(Report report) {
        if (report.getTopic() == null)
            return report.getComment().getId();
        else
            return report.getTopic().getId();
    }

    public static List<ReportDTO> mapReportToDTO(List<Report> reports) {
        return reports.stream()
                .map(ReportMapper::buildReportTopicDTO)
                .collect(Collectors.toList());
    }
}
