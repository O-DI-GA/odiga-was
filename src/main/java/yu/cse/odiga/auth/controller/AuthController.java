package yu.cse.odiga.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import yu.cse.odiga.auth.application.AuthService;
import yu.cse.odiga.auth.dto.LoginDto;
import yu.cse.odiga.auth.dto.RefreshTokenDto;
import yu.cse.odiga.auth.dto.SignUpDto;
import yu.cse.odiga.global.util.DefaultResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201, "Sign up success", authService.signUp(signUpDto)));
    }

    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "login success", authService.login(loginDto)));
    }
//
    @RequestMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "reissue access token", authService.reIssue(refreshTokenDto)));
    }
}
