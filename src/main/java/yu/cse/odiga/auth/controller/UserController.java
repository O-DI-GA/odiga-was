package yu.cse.odiga.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.auth.application.UserService;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.dto.UserProfileUpdateDto;
import yu.cse.odiga.global.util.DefaultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {
	private final UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<?> myPage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new DefaultResponse<>(200, "User profile fetched successfully",
				userService.getUserProfile(customUserDetails.getUsername())));
	}

	@PutMapping("/profile/edit")
	public ResponseEntity<?> editProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@ModelAttribute UserProfileUpdateDto userProfileUpdateDto) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new DefaultResponse<>(200, "User profile updated successfully",
				userService.updateUserProfile(customUserDetails, userProfileUpdateDto)));
	}

	@GetMapping("/reviews")
	public ResponseEntity<?> findUserReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new DefaultResponse<>(200, "find user reviews", userService.findUserReviews(customUserDetails)));
	}
}