package yu.cse.odiga.history.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.VisitCountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/analyze/visitCounts")
public class VisitCountController {

    private final VisitCountService visitCountService;

    @GetMapping("hour/{storeId}")
    public ResponseEntity<?> getMonthlyHourlyVisitCounts(@PathVariable Long storeId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResponse<>(201, "Monthly Hourly VisitCounts", visitCountService.getMonthlyHourlyVisitCounts(storeId)));
    }

    @GetMapping("day/{storeId}")
    public ResponseEntity<?> getMonthlyWeeklyVisitCounts(@PathVariable Long storeId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResponse<>(201, "Monthly Day VisitCounts", visitCountService.getMonthlyDayVisitCounts(storeId)));
    }
}