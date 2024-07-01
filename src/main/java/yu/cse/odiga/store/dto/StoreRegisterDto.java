package yu.cse.odiga.store.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class StoreRegisterDto {
    private String storeName;
    private String phoneNumber;
    private String address;
    private int tableCount;
    private Double longitude; // 경도
    private Double latitude; // 위도

    private String storeCategory;
    private MultipartFile storeTitleImage;
    private List<MultipartFile> storeImage;
}