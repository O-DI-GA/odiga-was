package yu.cse.odiga.store.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.GptSalesRecommendCommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/owner/store/{storeId}/analysis/sales-advice")
public class GptSalesRecommendCommentController {

	private final GptSalesRecommendCommentService gptSalesRecommendCommentService;

	@GetMapping
	ResponseEntity<?> getSalesCommentByStoreIdAndRangeOfDate(@PathVariable Long storeId,
		@RequestParam LocalDateTime startDate,
		@RequestParam LocalDateTime endDate) {

		return ResponseEntity.ok()
			.body(new DefaultResponse<>(200, "매출 분석이 완료되었습니다.",
				gptSalesRecommendCommentService.getRecommendCommentByStoreId(storeId, startDate, endDate)));
	}
}
