package yu.cse.odiga.history.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.ReservationHistoryService;

@RestController
@RequestMapping("api/v1/analyze/reservationCounts")
@RequiredArgsConstructor
public class ReservationHistoryController {
    private final ReservationHistoryService reservationService;

    @GetMapping("{storeId}")
    public ResponseEntity<?> getYearlyReservationCounts(@PathVariable Long storeId) {
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Yearly ReservationCounts",
                reservationService.getYearlyReservationCounts(storeId)));
    }
}
