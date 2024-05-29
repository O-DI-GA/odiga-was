package yu.cse.odiga.store.dto;


import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.store.domain.StoreImage;

import java.util.List;

@Builder
@Getter
public class StoreRegisterDto {
    private String storeName;
    private String phoneNumber;
    private String address;
    private int tableCount;

    private MultipartFile storeTitleImage;
    private List<MultipartFile> storeImage;
}
