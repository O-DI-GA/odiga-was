package yu.cse.odiga.store.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.domain.TableOrderMenu;
import yu.cse.odiga.store.dto.TableOrderMenuHistoryDto;
import yu.cse.odiga.store.dto.TableOrderMenuListDto;

@Service
@RequiredArgsConstructor
public class TableOrderService {
    private final TableOrderRepository tableOrderRepository;

    public TableOrderMenuHistoryDto findTableOrderList(Long storeId, int tableNumber) {

        // 엄청난 쿼리가 발생할거 같은데 이게 맞나?
        TableOrder tableOrder = tableOrderRepository.findByStoreTable_StoreIdAndStoreTable_TableNumber(storeId,
                tableNumber).orElseThrow();

        List<TableOrderMenu> tableOrderMenuList = tableOrder.getTableOrderMenuList();
        List<TableOrderMenuListDto> tableOrderMenuListDtoList = new ArrayList<>();
        for (TableOrderMenu tableOrderMenu : tableOrderMenuList) {
            TableOrderMenuListDto tableOrderMenuDto = TableOrderMenuListDto.builder()
                    .menuName(tableOrderMenu.getMenu().getMenuName())
                    .menuCount(tableOrderMenu.getMenuCount())
                    .menuTotalPrice(tableOrderMenu.getMenu().getPrice() * tableOrderMenu.getMenuCount())
                    .build();
            tableOrderMenuListDtoList.add(tableOrderMenuDto);
        }

        return TableOrderMenuHistoryDto.builder()
                .totalOrderPrice(tableOrder.getTableTotalPrice())
                .tableOrderMenuListDtoList(tableOrderMenuListDtoList)
                .build();
    }

}
