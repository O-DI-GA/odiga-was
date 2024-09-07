package yu.cse.odiga.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.reservation.domain.Reservation;
import yu.cse.odiga.store.domain.Store;

import java.time.LocalDateTime;

@Builder
@Getter
public final class ReservationResponseDto {
    private final Long userId; // dto로 변환
    private final Long storeId; //
    private final LocalDateTime reservationDateTime; // 이름 좀 이상한데...
    private final int peopleCount;

    public static ReservationResponseDto from(Reservation reservation) {
        return ReservationResponseDto.builder()
                .userId(reservation.getUser().getId())
                .storeId(reservation.getStore().getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .peopleCount(reservation.getPeopleCount())
                .build();
    }
}
