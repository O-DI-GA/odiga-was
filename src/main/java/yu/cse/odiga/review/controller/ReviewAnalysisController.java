package yu.cse.odiga.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.review.application.ReviewService;
import yu.cse.odiga.review.dto.ReviewStatisticsDto;

@RestController
@RequestMapping("/api/v1/owner/store/{storeId}/analysis")
@RequiredArgsConstructor
public class ReviewAnalysisController {
    private final ReviewService reviewService;

    @GetMapping("/review-statistics")
    public ResponseEntity<?> getReviewStatistics(@PathVariable Long storeId) {
        ReviewStatisticsDto statistics = reviewService.getReviewStatistics(storeId);
        return ResponseEntity.ok(new DefaultResponse<>(200, storeId + " store review statistics", statistics));
    }
}
