package huberts.spring.forumapp.exception;


import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(value = CategoryDoesntExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String categoryDoesntExistHandler(CategoryDoesntExistException e) {
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

    @ExceptionHandler(value = AccountBlockedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String accountIsBlocked(AccountBlockedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CategoryTitleIsBlankOrEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String categoryTitleIsBlankOrEmpty(CategoryTitleIsBlankOrEmptyException e) {
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
}
