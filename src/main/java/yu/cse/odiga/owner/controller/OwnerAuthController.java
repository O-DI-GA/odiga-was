package yu.cse.odiga.owner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.owner.application.OwnerAuthService;
import yu.cse.odiga.owner.dto.OwnerLoginDto;
import yu.cse.odiga.owner.dto.OwnerSignUpDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/owner/auth")
public class OwnerAuthController {

    private final OwnerAuthService ownerAuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> ownerSignup(@RequestBody OwnerSignUpDto ownerSignUpDto) {
        System.out.println(ownerSignUpDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201, "Sign up success", ownerAuthService.ownerSignUp(ownerSignUpDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> ownerLogin(@RequestBody OwnerLoginDto ownerLoginDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "login in success", ownerAuthService.login(ownerLoginDto)));
    }
}