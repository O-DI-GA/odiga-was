package yu.cse.odiga.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.reservation.application.ReservationService;
import yu.cse.odiga.reservation.dto.ReservationRegisterDto;
import yu.cse.odiga.reservation.dto.ReservationResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{storeId}")
    public ResponseEntity<?> registerReservation(@PathVariable Long storeId,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestBody ReservationRegisterDto reservationRegisterDto) {
        reservationService.registerReservation(customUserDetails, storeId, reservationRegisterDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "create reservation", null));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reservationService.deleteReservation(customUserDetails, reservationId);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201,"delete reservation", null));
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<?> updateReservation(@PathVariable Long reservationId,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                               @RequestBody ReservationRegisterDto reservationRegisterDto) {
        reservationService.updateReservation(customUserDetails, reservationId, reservationRegisterDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201,"update reservation", null));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<?> findByStoreId(@PathVariable Long storeId) {
        List<ReservationResponseDto> reservationList =  reservationService.findByStoreId(storeId);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "reservationList find by storeId", reservationList));
    }
}
