package yu.cse.odiga.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.dto.ReviewRegisterDto;
import yu.cse.odiga.store.dto.ReviewResponseDto;
import yu.cse.odiga.store.application.ReviewService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store/{storeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/post")
    public ResponseEntity<?> registerReview(@PathVariable Long storeId, @ModelAttribute ReviewRegisterDto reviewRegisterDto) throws IOException {
        reviewService.registerReview(storeId, reviewRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResponse<>(201, "created review", null));
    }

    @GetMapping
    public ResponseEntity<?> findStoreReviews(@PathVariable Long storeId) {
        List<ReviewResponseDto> reviews = reviewService.findStoreReviews(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(new DefaultResponse<>(200, "find reviews", reviews));
    }
}