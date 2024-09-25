package yu.cse.odiga.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerStoreDetailDTO {
    private Long storeId;
    private String storeName;
    private String phoneNumber;
    private String address;
    private int tableCount;
    private Double longitude;
    private Double latitude;
    private String storeTitleImage;
    private List<String> storeImage;
    private String storeCategory;
    private int reviewCount;
}