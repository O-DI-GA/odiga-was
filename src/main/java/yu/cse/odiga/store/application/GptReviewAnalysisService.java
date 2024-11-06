package yu.cse.odiga.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.review.dao.ReviewRepository;
import yu.cse.odiga.review.domain.Review;
import yu.cse.odiga.store.dto.GPTRequestDto;
import yu.cse.odiga.store.dto.GPTResponseDto;
import yu.cse.odiga.store.dto.Message;
import yu.cse.odiga.store.dto.ReviewAnalysisResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GptReviewAnalysisService {

    private final RestTemplate restTemplate;
    private final ReviewRepository reviewRepository;

    @Value("${openai.api.model.fine}")
    private String GPT_MODEL;

    @Value("${openai.api.url}")
    private String URL;

    private static final String GPT_SYSTEM_CONTENT = "너는 리뷰를 통해서 가게의 개선점을 도출해주는 봇이야" +
            ".답변은 항상 아래 형식으로 작성해:\\n\\n1. 개선 사항 요약:\\n   - 문제: 문제 요약\\n   - 개선 제안: 제안 내용\\n\\" +
            "n2. 추가 의견:\\n   - 추가 의견 내용\"";

    public ReviewAnalysisResponseDto getReviewAnalysisByStoreId(Long storeId) {
        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        if (reviews.isEmpty()) {
            throw new BusinessLogicException("가게에 작성된 리뷰가 없습니다.", HttpStatus.BAD_REQUEST.value());
        }

        String reviewContent = getAllReviewContents(reviews);
        GPTRequestDto gptRequestDto = GPTRequestDto.builder()
                .model(GPT_MODEL)
                .messages(List.of(new Message("system", GPT_SYSTEM_CONTENT), new Message("user", reviewContent)))
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
