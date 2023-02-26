package huberts.spring.forumapp.exception;

public class TopicExistException extends RuntimeException{
    public TopicExistException(String message) {
        super(message);
    }
}