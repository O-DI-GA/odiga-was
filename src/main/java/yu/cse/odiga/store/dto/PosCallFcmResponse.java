package yu.cse.odiga.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PosCallFcmResponse {
	private final int tableNumber;
	private final String data;
}
