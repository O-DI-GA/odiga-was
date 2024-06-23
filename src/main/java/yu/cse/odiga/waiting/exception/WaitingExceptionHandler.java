package yu.cse.odiga.waiting.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yu.cse.odiga.global.util.ErrorResponse;

@RestControllerAdvice
public class WaitingExceptionHandler {

    @ExceptionHandler(AlreadyHasWaitingException.class)
    public ResponseEntity<?> alreadyHasWaitingExceptionHandler(AlreadyHasWaitingException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(409)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NotFoundWaitingException.class)
    public ResponseEntity<?> notFoundWaitingExceptionHandler(NotFoundWaitingException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(404)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(WaitingCodeValidateException.class)
    public ResponseEntity<?> waitingCodeValidateExceptionHandler(WaitingCodeValidateException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(400)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AlreadyEnterWaitingCodeException.class)
    public ResponseEntity<?> alreadyEnterWaitingCodeException(AlreadyEnterWaitingCodeException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(400)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AlreadyCancelWaitingException.class)
    public ResponseEntity<?> alreadyCancelWaitingException(AlreadyCancelWaitingException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(400)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(NotValidateTurnException.class)
    public ResponseEntity<?> notValidateTurnException(NotValidateTurnException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(400)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
