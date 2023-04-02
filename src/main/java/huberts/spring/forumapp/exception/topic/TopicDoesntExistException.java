package huberts.spring.forumapp.exception.topic;

public class TopicDoesntExistException extends RuntimeException{
    public TopicDoesntExistException(String message) {
        super(message);
    }
}