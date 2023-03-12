package huberts.spring.forumapp.exception;

public class ReportExistException extends RuntimeException {
    public ReportExistException(String message) {
        super(message);
    }
}