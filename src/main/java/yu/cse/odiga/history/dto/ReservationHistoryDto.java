package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationHistoryDto {
    private String month;
    private Long reservationCount;
}