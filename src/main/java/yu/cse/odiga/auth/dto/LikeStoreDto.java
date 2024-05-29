package yu.cse.odiga.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeStoreDto {

    private String userEmail;
    private String storeName;

}
