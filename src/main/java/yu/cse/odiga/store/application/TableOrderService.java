package yu.cse.odiga.store.application;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.dto.TableOrderMenuHistoryDto;
import yu.cse.odiga.store.type.PaymentStatus;

@Service
@RequiredArgsConstructor
public class TableOrderService {
    private final TableOrderRepository tableOrderRepository;
    private final StoreTableRepository storeTableRepository;

    public void createTableOrder(Long storeTableId) {
        StoreTable storeTable = storeTableRepository.findById(storeTableId).orElseThrow();

        if (storeTable.isTableEmpty()) {
            TableOrder tableOrder = TableOrder.builder()
                    .paymentStatus(PaymentStatus.PENDING)
                    .tableOrderMenuList(new ArrayList<>())
                    .storeTable(storeTable)
                    .build();

            tableOrderRepository.save(tableOrder);
            storeTable.changeTableStatusToInUse();
        }
    }

    public TableOrderMenuHistoryDto findTableOrderList(Long storeId, int tableNumber) { // 곧 삭제할 코드

        TableOrder tableOrder = tableOrderRepository.findByStoreTable_StoreIdAndStoreTable_TableNumber(
                storeId, tableNumber).orElseThrow();

        return TableOrderMenuHistoryDto.builder()
                .tableOrderHistoryId(tableOrder.getId())
                .totalOrderPrice(tableOrder.getTableTotalPrice())
                .tableOrderMenuListDtoList(TableOrderMenuHistoryDto.from((tableOrder.getTableOrderMenuList())))
                .build();
    }

    public TableOrderMenuHistoryDto findTableOrderHistoryByTableId(Long storeTableId) {
        // 상태가 결제아직 안한 상태만 불러와야함
        return null;
    }

    public TableOrderMenuHistoryDto findByTableOrderHistoryByTableOrderId(Long tableOrderId) {
        TableOrder tableOrder = tableOrderRepository.findById(tableOrderId).orElseThrow();

        return TableOrderMenuHistoryDto.builder()
                .tableOrderHistoryId(tableOrder.getId())
                .totalOrderPrice(tableOrder.getTableTotalPrice())
                .tableOrderMenuListDtoList(TableOrderMenuHistoryDto.from((tableOrder.getTableOrderMenuList())))
                .build();
    }
}
