package yu.cse.odiga.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeStoreDto {

    private String userEmail;
    private String storeName;

}
