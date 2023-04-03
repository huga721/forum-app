package huberts.spring.forumapp.exception.topic;

public class TopicIsClosedException extends RuntimeException{
    public TopicIsClosedException(String message) {
        super(message);
    }
}