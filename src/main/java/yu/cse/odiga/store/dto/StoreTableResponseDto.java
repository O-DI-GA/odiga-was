package yu.cse.odiga.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.store.domain.StoreTable;

@Getter
@Builder
@AllArgsConstructor
public final class StoreTableResponseDto {
	private final Long storeTableId;
	private final int tableNumber;
	private final int maxSeatCount;

	public static StoreTableResponseDto from(StoreTable storeTable) {
		return StoreTableResponseDto.builder()
			.storeTableId(storeTable.getId())
			.tableNumber(storeTable.getTableNumber())
			.maxSeatCount(storeTable.getMaxSeatCount())
			.build();
	}
}
