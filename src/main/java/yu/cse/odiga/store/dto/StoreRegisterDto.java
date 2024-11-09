package yu.cse.odiga.store.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;

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