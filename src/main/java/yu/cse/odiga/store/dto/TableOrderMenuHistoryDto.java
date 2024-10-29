package yu.cse.odiga.store.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.domain.TableOrderMenu;

@Builder
@Getter
public class TableOrderMenuHistoryDto {
	private Long tableOrderHistoryId;
	private int totalTableOrderPrice;
	private int tableNumber;
	private List<TableOrderMenuDto> tableOrderMenuListDto;

	public static List<TableOrderMenuDto> fromEntityList(List<TableOrderMenu> tableOrderMenuList) {
		return tableOrderMenuList.stream()
			.map(TableOrderMenuDto::from)
			.toList();
	}

	public static TableOrderMenuHistoryDto from(TableOrder tableOrder) {
		return TableOrderMenuHistoryDto.builder()
			.tableOrderHistoryId(tableOrder.getId())
			.tableNumber(tableOrder.getStoreTable().getTableNumber())
			.totalTableOrderPrice(tableOrder.getTableTotalPrice())
			.tableOrderMenuListDto(TableOrderMenuHistoryDto.fromEntityList((tableOrder.getTableOrderMenuList())))
			.build();
	}
}