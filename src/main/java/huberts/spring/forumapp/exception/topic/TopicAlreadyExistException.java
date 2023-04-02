package huberts.spring.forumapp.exception.topic;

public class TopicAlreadyExistException extends RuntimeException{
    public TopicAlreadyExistException(String message) {
        super(message);
    }
}