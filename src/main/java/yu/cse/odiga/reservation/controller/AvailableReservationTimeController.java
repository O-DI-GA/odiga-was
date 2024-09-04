package yu.cse.odiga.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.reservation.application.AvailableReservationTimeService;
import yu.cse.odiga.reservation.dto.AvailableReservationTimeDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner/reservation")
@RequiredArgsConstructor
public class AvailableReservationTimeController {

    private final AvailableReservationTimeService availableReservationTimeService;

    @PostMapping("{storeId}")
    public ResponseEntity<?> registerAvailableReservationTime(@PathVariable Long storeId,
                                                 @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                                 @RequestBody List<AvailableReservationTimeDto> availableReservationTimeDtoList) {
        availableReservationTimeService.registerAvailableReservationTime(ownerUserDetails, storeId, availableReservationTimeDtoList);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "register availableReservationTime", null));
    }


    @PostMapping("toggleAvailableReservationTime/{availableReservationTimeId}")
    public ResponseEntity<?> toggleAvailableReservationTime(@PathVariable Long availableReservationTimeId,
                                                            @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        availableReservationTimeService.toggleAvailableReservationTime(ownerUserDetails, availableReservationTimeId);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "toggle availableReservationTime available", null));
    }
}