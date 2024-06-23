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
import yu.cse.odiga.store.type.PaymentStatus;

@Service
@RequiredArgsConstructor
public class TableOrderService {
    private final TableOrderRepository tableOrderRepository;
    private final StoreTableRepository storeTableRepository;

    public TableOrderMenuHistoryDto findTableOrderList(Long storeId, int tableNumber) {

        // 엄청난 쿼리가 발생할거 같은데 이게 맞나?
        Optional<TableOrder> tableOrderOptional = tableOrderRepository.findByStoreTable_StoreIdAndStoreTable_TableNumber(
                storeId, tableNumber);

        TableOrder tableOrder;

        if (tableOrderOptional.isEmpty()) {
            StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumber(storeId, tableNumber)
                    .orElseThrow();
            tableOrder = TableOrder.builder()
                    .storeTable(storeTable)
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();
            tableOrderRepository.save(tableOrder);
        } else {
            tableOrder = tableOrderOptional.get();
        }

        return TableOrderMenuHistoryDto.builder()
                .tableOrderHistoryId(tableOrder.getId())
                .totalOrderPrice(tableOrder.getTableTotalPrice())
                .tableOrderMenuListDtoList(
                        tableOrderMenuListToTableOrderMenuListDtoList(tableOrder.getTableOrderMenuList()))
                .build();
    }

    public TableOrderMenuHistoryDto findByIdOrderHistory(Long tableOrderHistoryId) {
        TableOrder tableOrder = tableOrderRepository.findById(tableOrderHistoryId).orElseThrow();

        return TableOrderMenuHistoryDto.builder()
                .tableOrderHistoryId(tableOrder.getId())
                .tableOrderMenuListDtoList(
                        tableOrderMenuListToTableOrderMenuListDtoList(tableOrder.getTableOrderMenuList()))
                .build();
    }

    public List<TableOrderMenuListDto> tableOrderMenuListToTableOrderMenuListDtoList(
            List<TableOrderMenu> tableOrderMenuList) {
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
