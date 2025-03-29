package ostro.veda.product_ms.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<String> handleDocumentAlreadyExistsException(DocumentAlreadyExistsException documentAlreadyExistsException) {
        return new ResponseEntity<>(documentAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<String> handleDocumentNotFoundException(DocumentNotFoundException documentNotFoundException) {
        return new ResponseEntity<>(documentNotFoundException.getMessage(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException(InvalidDataException invalidDataException) {
        return new ResponseEntity<>(invalidDataException.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleInvalidDataException(Throwable throwable) {
        return new ResponseEntity<>(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
