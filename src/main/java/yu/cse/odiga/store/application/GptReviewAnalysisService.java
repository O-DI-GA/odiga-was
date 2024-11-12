package yu.cse.odiga.store.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.review.dao.ReviewRepository;
import yu.cse.odiga.review.domain.Review;
import yu.cse.odiga.store.dto.GPTRequestDto;
import yu.cse.odiga.store.dto.GPTResponseDto;
import yu.cse.odiga.store.dto.Message;
import yu.cse.odiga.store.dto.ReviewAnalysisResponseDto;

@Service
@RequiredArgsConstructor
public class GptReviewAnalysisService {

	private final RestTemplate restTemplate;
	private final ReviewRepository reviewRepository;

	@Value("${openai.api.model.fine}")
	private String GPT_MODEL;

	@Value("${openai.api.url}")
	private String URL;

	private static final String GPT_SYSTEM_CONTENT_TEMPLATE = """
		## 문제 요약
		- 고객이 제기한 문제를 간단히 요약합니다.
		
		## 문제 해결 방법
		1. 첫 번째 문제 해결 방법을 명확하게 서술합니다.
		2. 두 번째 문제 해결 방법을 명확하게 서술합니다.
		3. 세 번째 문제 해결 방법을 명확하게 서술합니다.
		
		## 추가 제안 사항
		- 문제와 관련된 추가적인 개선 방안을 제안합니다.
		- 고객의 전반적인 만족도를 높이기 위한 구체적인 제안을 추가합니다.
		""";

	private static final double GPT_TEMPERATURE = 0.5;

	public ReviewAnalysisResponseDto getReviewAnalysisByStoreId(Long storeId) {
		List<Review> reviews = reviewRepository.findByStoreId(storeId);

		if (reviews.isEmpty()) {
			throw new BusinessLogicException("가게에 작성된 리뷰가 없습니다.", HttpStatus.BAD_REQUEST.value());
		}

		String reviewContent = getAllReviewContents(reviews);

		GPTRequestDto gptRequestDto = GPTRequestDto.builder()
			.model(GPT_MODEL)
			.temperature(GPT_TEMPERATURE)
			.messages(List.of(new Message("system", GPT_SYSTEM_CONTENT_TEMPLATE), new Message("user", reviewContent)))
			.build();

		GPTResponseDto gptResponseDto = restTemplate.postForObject(URL, gptRequestDto, GPTResponseDto.class);

		return new ReviewAnalysisResponseDto(gptResponseDto.getChoices().get(0).getMessage().getContent());
	}

	private String getAllReviewContents(List<Review> reviews) {
		StringBuilder sb = new StringBuilder();
		List<String> reviewContents = reviews.stream().map(Review::getContent).toList();

		for (String content : reviewContents) {
			sb.append(content);
		}
		return sb.toString();
	}
}
