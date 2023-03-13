package huberts.spring.forumapp.exception.topic;

public class TopicExistException extends RuntimeException{
    public TopicExistException(String message) {
        super(message);
    }
}