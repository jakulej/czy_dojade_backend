package ziwg.czy_dojade_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ErrorDetails getErrorDetails(Exception e, WebRequest request) {
        return new ErrorDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundException(NotFoundException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleAlreadyExistsException(AlreadyExistsException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handlePasswordMismatchException(BadCredentialsException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadySubscribedException.class)
    public ResponseEntity<ErrorDetails> handleAlreadSubscribedException(AlreadySubscribedException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotSubscribedException.class)
    public ResponseEntity<ErrorDetails> handleNotSubscribedException(NotSubscribedException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MaxFavouriteRoutesReachedException.class)
    public ResponseEntity<ErrorDetails> handleMaxFavouriteRoutesReachedException(MaxFavouriteRoutesReachedException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity<ErrorDetails> handleLimitExceededException(LimitExceededException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDetails> handleIOException(IOException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(e, request);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
