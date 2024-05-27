package yu.cse.odiga.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.auth.application.ImageService;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.dto.UserProfileDto;
import yu.cse.odiga.global.util.DefaultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/mypage")
public class MyPageController {

    private final ImageService imageService;

    @GetMapping("/profile")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        UserProfileDto userProfile = imageService.getUserProfile(userDetails.getUsername());


        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "User profile fetched successfully", userProfile));
    }
}