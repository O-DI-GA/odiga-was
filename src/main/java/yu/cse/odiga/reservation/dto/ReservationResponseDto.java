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
    private final User user; // dto로 변환
    private final Store store; //
    private final LocalDateTime reservationDate;
    private final int peopleCount;

    public static ReservationResponseDto from(Reservation reservation) {
        return ReservationResponseDto.builder()
                .user(reservation.getUser())
                .store(reservation.getStore())
                .reservationDate(reservation.getReservationDateTime())
                .peopleCount(reservation.getPeopleCount())
                .build();
    }
}
