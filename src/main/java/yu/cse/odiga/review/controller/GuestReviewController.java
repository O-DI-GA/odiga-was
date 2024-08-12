package yu.cse.odiga.review.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.review.application.ReviewService;
import yu.cse.odiga.review.dto.ReviewResponseDto;

@RestController
@RequestMapping("/api/v1/store/{storeId}/reviews")
@RequiredArgsConstructor
public class GuestReviewController {

    private final ReviewService reviewService;

    @GetMapping()
    public ResponseEntity<?> findStoreReviews(@PathVariable Long storeId) {
        List<ReviewResponseDto> reviews = reviewService.findStoreReviews(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(new DefaultResponse<>(200, "find reviews", reviews));
    }
}
