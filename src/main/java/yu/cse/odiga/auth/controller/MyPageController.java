package yu.cse.odiga.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import yu.cse.odiga.auth.application.ImageService;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import yu.cse.odiga.auth.dto.UserProfileDto;
import yu.cse.odiga.global.util.DefaultResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/user/mypage")
public class MyPageController {

    private final ImageService imageService;

    @RequestMapping("/profile")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        UserProfileDto userProfile = imageService.getUserProfile(customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "User profile fetched successfully", userProfile));
    }
}