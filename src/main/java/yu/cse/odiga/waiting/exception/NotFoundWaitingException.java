package yu.cse.odiga.waiting.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundWaitingException extends RuntimeException {
    private String message;
}
