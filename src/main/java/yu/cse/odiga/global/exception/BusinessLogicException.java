package yu.cse.odiga.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessLogicException extends RuntimeException {

	private final String message;
	private final int httpStatusCode;
}