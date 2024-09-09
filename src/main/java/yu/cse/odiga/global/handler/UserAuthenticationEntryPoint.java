package yu.cse.odiga.global.handler;

import java.io.IOException;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import yu.cse.odiga.global.util.ErrorResponse;

public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String AUTH_ERROR_MESSAGE = "인증 정보가 없습니다. 다시 로그인 해 주세요.";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		String errorMessage = authException.getMessage();

		if (authException instanceof InsufficientAuthenticationException) {
			errorMessage = AUTH_ERROR_MESSAGE;
		}

		ErrorResponse errorResponse = ErrorResponse.builder()
			.httpStatusCode(401)
			.errorMessage(errorMessage)
			.build();

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("content-type", "application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
		response.getWriter().flush();
		response.getWriter().close();
	}
}
