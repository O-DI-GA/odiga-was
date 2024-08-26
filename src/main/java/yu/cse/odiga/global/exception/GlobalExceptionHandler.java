package yu.cse.odiga.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yu.cse.odiga.global.util.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<?> businessLogicExceptionHandler(BusinessLogicException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatusCode(ex.getHttpStatusCode())
                .errorMessage(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getHttpStatusCode()));
    }
}

