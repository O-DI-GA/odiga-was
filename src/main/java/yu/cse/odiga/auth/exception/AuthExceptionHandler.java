package yu.cse.odiga.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yu.cse.odiga.global.util.ErrorResponse;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AlreadyExistUserException.class)
    public ResponseEntity<?> alreadyExistUserExceptionHandler(AlreadyExistUserException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(409)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundExceptionHandler(UsernameNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(404)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(400)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(400)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
