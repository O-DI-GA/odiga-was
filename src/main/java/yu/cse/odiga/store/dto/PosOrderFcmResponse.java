package yu.cse.odiga.store.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PosOrderFcmResponse {
	private final int tableNumber;
	private final List<TableOrderMenuforManage> data;
}
