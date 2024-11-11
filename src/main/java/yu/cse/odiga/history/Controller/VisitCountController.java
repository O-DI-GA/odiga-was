package yu.cse.odiga.history.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.VisitCountService;
import yu.cse.odiga.owner.domain.OwnerUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/owner/store/{storeId}/analysis")
public class VisitCountController {

    private final VisitCountService visitCountService;

    @GetMapping("monthly-hourly-visit-counts")
    public ResponseEntity<?> getMonthlyHourlyVisitCounts(@PathVariable Long storeId, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResponse<>(201, "Monthly Hourly VisitCounts", visitCountService.getMonthlyHourlyVisitCounts(storeId, ownerUserDetails)));
    }

    @GetMapping("monthly-day-visit-counts")
    public ResponseEntity<?> getMonthlyWeeklyVisitCounts(@PathVariable Long storeId, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResponse<>(201, "Monthly Day VisitCounts", visitCountService.getMonthlyDayVisitCounts(storeId, ownerUserDetails)));
    }

    @GetMapping("today-hourly-visit-counts")
    public ResponseEntity<?> getTodayHourlyVisitCounts(@PathVariable Long storeId, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResponse<>(201, "Today Hourly VisitCounts", visitCountService.getTodayHourlyVisitCounts(storeId, ownerUserDetails)));
    }
}