package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingHistoryDto {
    private String hour;
    private Double waitingCount;
}