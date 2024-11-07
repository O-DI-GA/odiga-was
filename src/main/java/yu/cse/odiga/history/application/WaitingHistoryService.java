package yu.cse.odiga.history.application;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.history.dao.WaitingHistoryRepository;
import yu.cse.odiga.history.dto.WaitingHistoryDto;
import yu.cse.odiga.waiting.domain.Waiting;

@Service
@RequiredArgsConstructor
public class WaitingHistoryService {
    private final WaitingHistoryRepository waitingHistoryRepository;

    public Map<String, List<WaitingHistoryDto>> getMonthlyHourlyAverageWaitingCounts(Long storeId) {
        List<Waiting> waitings = waitingHistoryRepository.findByStoreId(storeId)
                .orElseThrow(() -> new BusinessLogicException("존재하지 않는 storeId 입니다.", HttpStatus.BAD_REQUEST.value()));

        Map<String, Map<String, Double>> monthlyHourlyAverages = waitings.stream()
                .collect(Collectors.groupingBy(
                        waiting -> YearMonth.from(waiting.getCreatedAt()).toString(),
                        Collectors.groupingBy(
                                waiting -> waiting.getCreatedAt().toLocalTime().truncatedTo(ChronoUnit.HOURS).toString(),
                                Collectors.collectingAndThen(
                                        Collectors.groupingBy(
                                                waiting -> waiting.getCreatedAt().toLocalDate(),
                                                Collectors.counting()
                                        ),
                                        dateCountMap -> dateCountMap.values().stream()
                                                .mapToLong(Long::longValue)
                                                .average()
                                                .orElse(0.0)
                                )
                        )
                ));

        return monthlyHourlyAverages.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .map(hourEntry -> new WaitingHistoryDto(hourEntry.getKey(), hourEntry.getValue()))
                                .collect(Collectors.toList())
                ));
    }

    public List<WaitingHistoryDto> getTodayHourlyWaitingCounts(Long storeId) {
        List<Waiting> waitings = waitingHistoryRepository.findByStoreId(storeId)
                .orElseThrow(() -> new BusinessLogicException("존재하지 않는 storeId 입니다.", HttpStatus.BAD_REQUEST.value()));

        Map<String, Long> hourlyCountsToday = waitings.stream()
                .filter(waiting -> waiting.getCreatedAt().toLocalDate().equals(LocalDate.now())) // Filter by today's date
                .collect(Collectors.groupingBy(
                        waiting -> waiting.getCreatedAt().toLocalTime().truncatedTo(ChronoUnit.HOURS).toString(), // Group by hour
                        Collectors.counting()
                ));

        return hourlyCountsToday.entrySet().stream()
                .map(entry -> new WaitingHistoryDto(entry.getKey(), entry.getValue().doubleValue()))
                .collect(Collectors.toList());
    }
}