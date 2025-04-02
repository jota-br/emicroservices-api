package ostro.veda.user_ms.handler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthorizationDeniedException(AuthException authException) {
        return new ResponseEntity<>(authException.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException authorizationDeniedException) {
        return new ResponseEntity<>(authorizationDeniedException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleEntityExistsException(EntityExistsException entityExistsException) {
        return new ResponseEntity<>(entityExistsException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Throwable throwable) {
        return new ResponseEntity<>(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
