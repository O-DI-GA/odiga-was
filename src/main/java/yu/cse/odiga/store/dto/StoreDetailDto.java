package yu.cse.odiga.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreDetailDto {
    private String storeName;
    private String storeTitleImage;
    private double averageRating;
    private int tableCount;
    private int waitingCount;
    private int likeCount;
    private String phoneNumber;
    private String address;
    private int emptyTableCount;
}
