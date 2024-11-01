package yu.cse.odiga.waiting.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OwnerWaitingDto {
    private Long waitingId;
    private int waitingNumber;
    private int peopleCount;
}
