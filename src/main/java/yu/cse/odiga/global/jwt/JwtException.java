package yu.cse.odiga.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtException extends RuntimeException {
	private String message;
}
