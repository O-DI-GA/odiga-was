package yu.cse.odiga.history.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.ReservationHistoryService;
import yu.cse.odiga.owner.domain.OwnerUserDetails;

@RestController
@RequestMapping("api/v1/owner/store/{storeId}/analysis")
@RequiredArgsConstructor
public class ReservationHistoryController {
    private final ReservationHistoryService reservationService;

    @GetMapping("reservation-counts")
    public ResponseEntity<?> getYearlyReservationCounts(@PathVariable Long storeId, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Yearly ReservationCounts",
                reservationService.getYearlyReservationCounts(storeId, ownerUserDetails)));
    }
}
