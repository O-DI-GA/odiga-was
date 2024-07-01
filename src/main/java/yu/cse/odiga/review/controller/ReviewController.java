package yu.cse.odiga.review.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.review.application.ReviewService;
import yu.cse.odiga.review.dto.ReviewRegisterDto;

@RestController
@RequestMapping("/api/v1/user/store/{storeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<?> registerReview(@PathVariable Long storeId, @AuthenticationPrincipal CustomUserDetails customUserDetails, @ModelAttribute ReviewRegisterDto reviewRegisterDto) throws IOException {
        reviewService.registerReview(storeId, reviewRegisterDto, customUserDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResponse<>(201, "created review", null));
    }
}