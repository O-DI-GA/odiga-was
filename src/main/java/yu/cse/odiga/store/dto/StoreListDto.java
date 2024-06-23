package yu.cse.odiga.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreListDto {
    private String storeTitleImage;
    private String storeName;
    private Long storeId;
    private int likeCount;
    private int reviewCount;
    private int waitingCount;
    private String storeCategory;
}
