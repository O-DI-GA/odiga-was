package yu.cse.odiga.store.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.store.domain.TableOrderMenu;

@Builder
@Getter
public class TableOrderMenuHistoryDto {
    private Long tableOrderHistoryId;
    private int totalOrderPrice;
    private List<TableOrderMenuListDto> tableOrderMenuListDtoList;

    public static List<TableOrderMenuListDto> from(List<TableOrderMenu> tableOrderMenuList) {
        List<TableOrderMenuListDto> tableOrderMenuListDtoList = new ArrayList<>();
        for (TableOrderMenu tableOrderMenu : tableOrderMenuList) {
            TableOrderMenuListDto tableOrderMenuDto = TableOrderMenuListDto.builder()
                    .menuName(tableOrderMenu.getMenu().getMenuName())
                    .menuCount(tableOrderMenu.getMenuCount())
                    .menuImageUrl(tableOrderMenu.getMenu().getMenuImageUrl())
                    .menuTotalPrice(tableOrderMenu.getMenu().getPrice() * tableOrderMenu.getMenuCount())
                    .build();
            tableOrderMenuListDtoList.add(tableOrderMenuDto);
        }

        return tableOrderMenuListDtoList;
    }
}


