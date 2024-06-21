package yu.cse.odiga.waiting.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserWaitingDto {
    private Long waitingId;
    private Long storeId;
    private String storeName;
    private int previousWaitingCount;
    private String storeTitleImage;
}
