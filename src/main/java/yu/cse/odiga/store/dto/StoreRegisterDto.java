package yu.cse.odiga.store.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreRegisterDto {
    private String storeName;
    private String phoneNumber;
    private String address;
    private int tableCount;
    // 추후 사진 추가
}
