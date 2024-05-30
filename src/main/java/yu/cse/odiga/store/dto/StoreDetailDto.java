package yu.cse.odiga.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreDetailDto {
    private String storeName;
    private int tableCount;
    private int waitingCount;
    private String phoneNumber;
    private String address;
    private int emptyTableCount;
}
