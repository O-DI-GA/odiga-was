package yu.cse.odiga.history.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.WaitingHistoryService;

@RestController
@RequestMapping("api/v1/analyze/waitingCounts")
@RequiredArgsConstructor
public class WaitingHistoryController {
    private final WaitingHistoryService waitingHistoryService;

    @GetMapping("month/{storeId}")
    public ResponseEntity<?> getMonthlyHourlyAverageWaitingCounts(@PathVariable Long storeId) {
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Monthly Hourly Average WaitingCounts",
                waitingHistoryService.getMonthlyHourlyAverageWaitingCounts(storeId)));
    }

    @GetMapping("today/{storeId}")
    public ResponseEntity<?> getTodayHourlyWaitingCounts(@PathVariable Long storeId) {
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Today's Hourly Waiting Counts",
                waitingHistoryService.getTodayHourlyWaitingCounts(storeId)));
    }
}