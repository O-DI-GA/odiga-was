package yu.cse.odiga.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.auth.application.UserAuthService;
import yu.cse.odiga.auth.dto.LoginDto;
import yu.cse.odiga.auth.dto.RefreshTokenDto;
import yu.cse.odiga.auth.dto.SignUpDto;
import yu.cse.odiga.global.util.DefaultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/auth")
public class AuthController {
	private final UserAuthService userAuthService;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new DefaultResponse<>(201, "Sign up success", userAuthService.signUp(signUpDto)));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new DefaultResponse<>(200, "login success", userAuthService.login(loginDto)));
	}

	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(@RequestBody RefreshTokenDto refreshTokenDto) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new DefaultResponse<>(200, "reissue access token", userAuthService.reIssue(refreshTokenDto)));
	}
}