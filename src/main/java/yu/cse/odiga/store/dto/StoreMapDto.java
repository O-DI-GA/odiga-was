package yu.cse.odiga.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreMapDto {
    private Long storeId;
    private String storeName;
    private Double longitude;
    private Double latitude;
    private int emptyTableCount;
    private int waitingCount;
    private String storeCategory;
}
