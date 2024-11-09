package yu.cse.odiga.store.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.domain.TableOrderMenu;

@Builder
@Getter
public class TableOrderMenuHistoryDto {
	private Long tableOrderHistoryId;
	private int totalTableOrderPrice;
	private int tableNumber;
	private List<TableOrderMenuDto> tableOrderMenus;
	private int tableCount;

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
			.tableOrderMenus(TableOrderMenuHistoryDto.fromEntityList((tableOrder.getTableOrderMenuList())))
			.build();
	}

	public static TableOrderMenuHistoryDto of(TableOrder tableOrder, Store store) {
		return TableOrderMenuHistoryDto.builder()
			.tableCount(store.getTables().size())
			.tableOrderHistoryId(tableOrder.getId())
			.tableNumber(tableOrder.getStoreTable().getTableNumber())
			.totalTableOrderPrice(tableOrder.getTableTotalPrice())
			.tableOrderMenus(TableOrderMenuHistoryDto.fromEntityList((tableOrder.getTableOrderMenuList())))
			.build();
	}
}