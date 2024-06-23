package yu.cse.odiga.store.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class TableOrderMenuHistoryDto {
    private Long tableOrderHistoryId;
    private List<TableOrderMenuListDto> tableOrderMenuListDtoList;
    private int totalOrderPrice;
}
