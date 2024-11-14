package yu.cse.odiga.store.application;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.history.dao.HistoryMenuRepository;
import yu.cse.odiga.history.dto.StatisticsResponseDto;
import yu.cse.odiga.store.dto.GPTCommentResponseDto;
import yu.cse.odiga.store.dto.GPTRequestDto;
import yu.cse.odiga.store.dto.GPTResponseDto;
import yu.cse.odiga.store.dto.Message;

@Service
@RequiredArgsConstructor
public class GptSalesRecommendCommentService {

	private final RestTemplate restTemplate;
	private final HistoryMenuRepository historyMenuRepository;

	@Value("${openai.api.model.mini}")
	private String GPT_MODEL;

	@Value("${openai.api.url}")
	private String URL;

	private static final double GPT_TEMPERATURE = 0.5;

	private static final String GPT_SYSTEM_CONTENT = """
		## 전체 매출 평가
		1. 매출 평가를 명확하게 서술합니다.
		2. 매출 평가를 명확하게 서술합니다.
		3. 매출 평가를 명확하게 서술합니다.
		
		## 매출에 대한 조언
		1. 매출에 대한 조언을 명확하게 서술합니다.
		2. 매출에 대한 조언을 명확하게 서술합니다.
		3. 매출에 대한 조언을 명확하게 서술합니다.
		""";

	public GPTCommentResponseDto getRecommendCommentByStoreId(Long storeId, LocalDateTime startDate,
		LocalDateTime endTime) {
		List<StatisticsResponseDto> categoryHistory = historyMenuRepository.getCategorySalesStatisticsByStoreIdAndDateRange(
			storeId, startDate, endTime);

		HashMap<String, Long> requestJsonMap = new HashMap<>();

		for (StatisticsResponseDto statisticsResponseDto : categoryHistory) {
			requestJsonMap.put(statisticsResponseDto.getName(), statisticsResponseDto.getTotalSalesAmount());
		}

		Gson gson = new Gson();

		String json = gson.toJson(requestJsonMap);

		String prompt = """
			아래는 가게의 매출데이터입니다. 이 데이터를 바탕으로 전체적인 매출에 대한 종합적인 평가와 매출 개선 방안을 제안해 주세요.
			매출 데이터
			""" + json + """
			템플릿을 따라 주세요.
			중요한 단어에는 blod체로 답변을 해주세요.
			""" + GPT_SYSTEM_CONTENT;

		GPTRequestDto gptRequestDto = GPTRequestDto.builder()
			.model(GPT_MODEL)
			.temperature(GPT_TEMPERATURE)
			.messages(List.of(new Message("system", GPT_SYSTEM_CONTENT), new Message("user", prompt)))
			.build();

		GPTResponseDto gptResponseDto = restTemplate.postForObject(URL, gptRequestDto, GPTResponseDto.class);

		return new GPTCommentResponseDto(gptResponseDto.getChoices().get(0).getMessage().getContent());
	}

}
