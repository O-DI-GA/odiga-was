package yu.cse.odiga.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.store.domain.TableOrder;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableOrderHistoryDto {
    private Long storeTableId;
    private int totalStoreTablePrice;
    private List<TableOrderMenuHistoryDto> tableOrderMenuHistoryDtoList;

    public static TableOrderHistoryDto from(List<TableOrder> tableOrderList) {
        int totalStoreTablePrice = tableOrderList.stream()
                .mapToInt(TableOrder::getTableTotalPrice)
                .sum();

        List<TableOrderMenuHistoryDto> tableOrderMenuHistoryDtoList = tableOrderList.stream()
                .map(TableOrderMenuHistoryDto::from)
                .toList();

        return TableOrderHistoryDto.builder()
                .storeTableId(tableOrderList.get(0).getStoreTable().getId())
                .totalStoreTablePrice(totalStoreTablePrice)
                .tableOrderMenuHistoryDtoList(tableOrderMenuHistoryDtoList)
                .build();
    }
}