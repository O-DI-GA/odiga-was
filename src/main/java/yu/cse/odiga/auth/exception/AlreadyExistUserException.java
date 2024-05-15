package yu.cse.odiga.auth.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlreadyExistUserException extends RuntimeException {
    private String message;
}
