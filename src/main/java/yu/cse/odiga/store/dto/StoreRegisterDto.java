package yu.cse.odiga.store.dto;


import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
public class StoreRegisterDto {
    private String storeName;
    private String phoneNumber;
    private String address;
    private int tableCount;

    private Double longitude; // 경도
    private Double latitude; // 위도

    private MultipartFile storeTitleImage;
    private List<MultipartFile> storeImage;
}
