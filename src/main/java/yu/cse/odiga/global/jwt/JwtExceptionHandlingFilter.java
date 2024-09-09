package yu.cse.odiga.global.jwt;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import yu.cse.odiga.global.util.ErrorResponse;

@Component
public class JwtExceptionHandlingFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException ex) {
			setResponse(response, ex.getMessage());
		}
	}

	private void setResponse(HttpServletResponse response, String errorMessage) throws IOException {
		ErrorResponse errorResponse = ErrorResponse.builder()
			.httpStatusCode(401)
			.errorMessage(errorMessage)
			.build();

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
		response.getWriter().flush();
		response.getWriter().close();
	}
}
