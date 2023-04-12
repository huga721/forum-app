package huberts.spring.forumapp.exception.handler;


import huberts.spring.forumapp.exception.category.CategoryAlreadyExistException;
import huberts.spring.forumapp.exception.category.CategoryDescriptionException;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.category.CategoryTitleException;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.user.UserIsNotAuthorException;
import huberts.spring.forumapp.exception.like.LikeExistException;
import huberts.spring.forumapp.exception.report.ReportExistException;
import huberts.spring.forumapp.exception.report.ReportRealiseException;
import huberts.spring.forumapp.exception.role.RoleDoesntExistException;
import huberts.spring.forumapp.exception.role.RoleException;
import huberts.spring.forumapp.exception.topic.*;
import huberts.spring.forumapp.exception.user.*;
import huberts.spring.forumapp.exception.warning.WarningExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerHandler {

    @ExceptionHandler(value = UserAlreadyExistingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userAlreadyExistingHandler(UserAlreadyExistingException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CategoryAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String categoryAlreadyExistingHandler(CategoryAlreadyExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CategoryDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String categoryDoesntExistHandler(CategoryDoesntExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = UsernameOrPasswordIsBlankOrEmpty.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String usernameIsBlankOrEmpty(UsernameOrPasswordIsBlankOrEmpty e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = UserDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
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

    @ExceptionHandler(value = TopicAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String topicDoesntExist(TopicAlreadyExistException e) {
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

    @ExceptionHandler(value = CommentDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String commentExist(CommentDoesntExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String argumentNotValid(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().get();
        MethodParameter parameter = e.getParameter();
        log.error("An exception occurred during validation of method arguments!", new MethodArgumentNotValidException(parameter, bindingResult));
        return "An exception occurred during validation of method arguments: " + errorMessage;
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

    @ExceptionHandler(value = UserAdminDeleteException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String userDeleteException(UserAdminDeleteException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = TopicDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String topicDoesntExistException(TopicDoesntExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = TopicIsClosedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String topicIsClosedException (TopicIsClosedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = UserIsNotAuthorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userIsNotAuthorOfCommentException (UserIsNotAuthorException e) {
        return e.getMessage();
    }
}