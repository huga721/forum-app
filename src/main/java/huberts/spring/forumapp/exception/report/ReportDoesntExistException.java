package huberts.spring.forumapp.exception.report;

public class ReportDoesntExistException extends RuntimeException {
    public ReportDoesntExistException(String message) {
        super(message);
    }
}