package huberts.spring.forumapp.exception.report;

public class ReportExistException extends RuntimeException {
    public ReportExistException(String message) {
        super(message);
    }
}