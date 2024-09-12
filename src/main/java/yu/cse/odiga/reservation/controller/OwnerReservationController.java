package yu.cse.odiga.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.reservation.application.OwnerReservationService;
import yu.cse.odiga.reservation.dto.AvailableReservationTimeDto;
import yu.cse.odiga.reservation.dto.ReservationRegisterDto;
import yu.cse.odiga.reservation.dto.ReservationResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner/reservation")
@RequiredArgsConstructor
public class OwnerReservationController {

    private final OwnerReservationService ownerReservationService;

    // 예약 가능 시간 등록
    @PostMapping("{storeId}")
    public ResponseEntity<?> registerAvailableReservationTime(@PathVariable Long storeId,
                                                 @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                                 @RequestBody List<AvailableReservationTimeDto> availableReservationTimeDtoList) {
        ownerReservationService.registerAvailableReservationTime(ownerUserDetails, storeId, availableReservationTimeDtoList);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "register availableReservationTime", null));
    }

    // 예약 가능 시간 목록
    @GetMapping("{storeId}/availableReservationTime")
    public ResponseEntity<?> getAvailableReservationTime(@PathVariable Long storeId,
                                                         @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "get availableReservationTimes", ownerReservationService.getAvailableReservationTimes(ownerUserDetails, storeId)));
    }

    // 예약 가능 시간 삭제
    @DeleteMapping("availableReservationTime/{availableReservationTimeId}")
    public ResponseEntity<?> deleteAvailableReservationTime(@PathVariable Long availableReservationTimeId,
                                                            @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        ownerReservationService.deleteAvailableReservationTime(ownerUserDetails, availableReservationTimeId);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "delete availableReservationTime", null));
    }

    // 예약 가능 전환
    @PostMapping("toggleAvailableReservationTime/{availableReservationTimeId}")
    public ResponseEntity<?> toggleAvailableReservationTime(@PathVariable Long availableReservationTimeId,
                                                            @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        ownerReservationService.toggleAvailableReservationTime(ownerUserDetails, availableReservationTimeId);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "toggle availableReservationTime available", null));
    }

    // 예약 가능 시간 수정
    @PutMapping("{storeId}/availableReservationTime/{availableReservationTimeId}")
    public ResponseEntity<?> updateAvailableReservation(@PathVariable Long availableReservationTimeId,
                                                        @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                                        @RequestBody AvailableReservationTimeDto availableReservationTimeDto) {
        ownerReservationService.updateAvailableReservation(ownerUserDetails, availableReservationTimeId, availableReservationTimeDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201,"update availableReservation", null));
    }

    // 가게 예약 목록
    @GetMapping("/{storeId}")
    public ResponseEntity<?> findByStoreId(@PathVariable Long storeId,
                                           @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        List<ReservationResponseDto> reservationList =  ownerReservationService.findByStoreId(ownerUserDetails, storeId);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "reservationList find by storeId", reservationList));
    }

    // 예약 취소
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId,
                                               @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        ownerReservationService.deleteReservation(ownerUserDetails, reservationId);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201,"delete reservation", null));
    }

    // 예약 수정
    @PutMapping("/{reservationId}")
    public ResponseEntity<?> updateReservation(@PathVariable Long reservationId,
                                               @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                               @RequestBody ReservationRegisterDto reservationRegisterDto) {
        ownerReservationService.updateReservation(ownerUserDetails, reservationId, reservationRegisterDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201,"update reservation", null));
    }

}