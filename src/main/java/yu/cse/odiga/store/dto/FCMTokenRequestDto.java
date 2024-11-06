package yu.cse.odiga.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FCMTokenRequestDto {
	private Long storeId;
	private String fcmToken;
}
