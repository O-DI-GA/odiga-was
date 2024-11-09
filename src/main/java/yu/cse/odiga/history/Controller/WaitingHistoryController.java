package yu.cse.odiga.history.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.WaitingHistoryService;
import yu.cse.odiga.owner.domain.OwnerUserDetails;

@RestController
@RequestMapping("api/v1/owner/store/{storeId}/analysis")
@RequiredArgsConstructor
public class WaitingHistoryController {
    private final WaitingHistoryService waitingHistoryService;

    @GetMapping("monthly-hourly-average-waiting-counts")
    public ResponseEntity<?> getMonthlyHourlyAverageWaitingCounts(@PathVariable Long storeId, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Monthly Hourly Average WaitingCounts",
                waitingHistoryService.getMonthlyHourlyAverageWaitingCounts(storeId, ownerUserDetails)));
    }

    @GetMapping("today-hourly-waiting-counts")
    public ResponseEntity<?> getTodayHourlyWaitingCounts(@PathVariable Long storeId, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Today's Hourly Waiting Counts",
                waitingHistoryService.getTodayHourlyWaitingCounts(storeId, ownerUserDetails)));
    }
}