package yu.cse.odiga.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreTableRegisterDto {
	private int tableNumber;
	private int maxSeatCount;
}
