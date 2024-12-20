package yu.cse.odiga.global.jwt;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import yu.cse.odiga.global.util.ErrorResponse;

public class UserAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ErrorResponse errorResponse = ErrorResponse.builder()
			.httpStatusCode(403)
			.errorMessage(accessDeniedException.getMessage())
			.build();

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setHeader("content-type", "application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
		response.getWriter().flush();
		response.getWriter().close();
	}
}
