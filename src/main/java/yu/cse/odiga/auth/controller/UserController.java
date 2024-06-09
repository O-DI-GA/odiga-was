package yu.cse.odiga.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.auth.application.UserService;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.dto.UserProfileDto;
import yu.cse.odiga.global.util.DefaultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileDto userProfile = userService.getUserProfile(userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "User profile fetched successfully", userProfile));
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<?> editProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @ModelAttribute MultipartFile profileImage,
                                         @RequestParam(required = false) String nickname){
        userService.updateUserProfile(userDetails.getUsername(), profileImage, nickname);
        UserProfileDto updatedProfile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "User profile updated successfully", updatedProfile));
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> findUserReviews(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(new DefaultResponse<>(200, "find user reviews", userService.findUserReviews(userDetails)));
    }
}