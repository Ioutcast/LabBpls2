package vasilkov.labbpls2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AuthException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "ExpiredJwtException", request);
    }

    @ExceptionHandler(ResourceIsNotValidException.class)
    public ResponseEntity<ErrorMessage> handleResourceIsNotValidException(ResourceIsNotValidException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "resourceIsNotValidException", request);
    }

    @ExceptionHandler(MyPSQLException.class)
    public ResponseEntity<ErrorMessage> handleMyPSQLException(MyPSQLException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "PSQLException", request);
    }

    @ExceptionHandler(MyConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleMyConstraintViolationException(MyConstraintViolationException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "MyConstraintViolationException", request);
    }

    private ResponseEntity<ErrorMessage> buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                status.value(),
                new Date(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorMessage, status);
    }
}