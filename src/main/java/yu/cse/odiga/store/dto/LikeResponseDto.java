package yu.cse.odiga.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDto {
	private Long storeId;
	private String storeName;
	private String address;
	private String phoneNumber;
	private String storeTitleImageUrl;
	private String storeCategory;
}
