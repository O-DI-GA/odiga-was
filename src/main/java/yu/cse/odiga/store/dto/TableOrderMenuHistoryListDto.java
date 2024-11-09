package yu.cse.odiga.store.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TableOrderMenuHistoryListDto {
	private int tableCount;
	private List<TableOrderMenuHistoryDto> orderHistoryList;
}
