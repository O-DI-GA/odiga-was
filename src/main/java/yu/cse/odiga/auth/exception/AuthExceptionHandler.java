package yu.cse.odiga.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
