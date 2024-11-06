package yu.cse.odiga.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.GptReviewAnalysisService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/owner/store/{storeId}")
public class GptReviewAnalysisController {
    private final GptReviewAnalysisService gptReviewAnalysisService;

    @GetMapping("review-analysis")
    public ResponseEntity<?> getReviewAnalysis(@PathVariable Long storeId) {

        return ResponseEntity.ok().body(
                new DefaultResponse<>(200, "리뷰 분석을 성공적으로 완료 했습니다", gptReviewAnalysisService.getReviewAnalysisByStoreId(storeId)));
    }
}
