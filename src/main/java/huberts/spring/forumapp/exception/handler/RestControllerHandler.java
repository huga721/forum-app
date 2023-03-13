package huberts.spring.forumapp.exception.handler;


import huberts.spring.forumapp.exception.category.CategoryAlreadyExistingException;
import huberts.spring.forumapp.exception.category.CategoryDescriptionException;
import huberts.spring.forumapp.exception.category.CategoryExistException;
import huberts.spring.forumapp.exception.category.CategoryTitleException;
import huberts.spring.forumapp.exception.comment.CommentExistException;
import huberts.spring.forumapp.exception.like.LikeExistException;
import huberts.spring.forumapp.exception.report.ReportExistException;
import huberts.spring.forumapp.exception.report.ReportRealiseException;
import huberts.spring.forumapp.exception.role.RoleDoesntExistException;
import huberts.spring.forumapp.exception.role.RoleException;
import huberts.spring.forumapp.exception.topic.TopicContentException;
import huberts.spring.forumapp.exception.topic.TopicExistException;
import huberts.spring.forumapp.exception.topic.TopicTitleException;
import huberts.spring.forumapp.exception.user.UserAlreadyExistingException;
import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.exception.user.UsernameOrPasswordIsBlankOrEmpty;
import huberts.spring.forumapp.exception.warning.WarningExistException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerHandler {

    @ExceptionHandler(value = UserAlreadyExistingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userAlreadyExistingHandler(UserAlreadyExistingException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CategoryAlreadyExistingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String categoryAlreadyExistingHandler(CategoryAlreadyExistingException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CategoryExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String categoryDoesntExistHandler(CategoryExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = UsernameOrPasswordIsBlankOrEmpty.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String usernameIsBlankOrEmpty(UsernameOrPasswordIsBlankOrEmpty e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = UserDoesntExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userDoesntExist(UserDoesntExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = RoleDoesntExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String roleDoesntExist(RoleDoesntExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CategoryTitleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String categoryTitleIsBlankOrEmpty(CategoryTitleException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = UserBlockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userBlocked(UserBlockException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = RoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userHasThatRole(RoleException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CategoryDescriptionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String categoryDescription(CategoryDescriptionException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = TopicExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String topicDoesntExist(TopicExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = TopicContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String topicContentExist(TopicContentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = TopicTitleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String topicTitle(TopicTitleException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = LikeExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String likeExist(LikeExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CommentExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String commentExist(CommentExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String argumentNotValid(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().get();
    }

    @ExceptionHandler(value = ReportExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String reportExist(ReportExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = ReportRealiseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cantRealiseReports(ReportRealiseException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = WarningExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cantRealiseReports(WarningExistException e) {
        return e.getMessage();
    }
}