package yu.cse.odiga.store.application;

import jakarta.transaction.Transactional;
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

    @Transactional
    public void createTableOrder(Long storeTableId) { // 테이블이 비어 있을 경우 이걸로 생성 해야함.
        StoreTable storeTable = storeTableRepository.findById(storeTableId)
                .orElseThrow(() -> new IllegalStateException("존재 하지 않는 store table id 입니다."));

        TableOrder tableOrder = TableOrder.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .tableOrderMenuList(new ArrayList<>())
                .storeTable(storeTable)
                .build();

        tableOrderRepository.save(tableOrder);
        storeTable.addNewTableOrder(tableOrder);
        storeTable.changeTableStatusToInUse();

    }

    public void checkEmptyTableByStoreTableId(Long storeTableId) {
        StoreTable storeTable = storeTableRepository.findById(storeTableId)
                .orElseThrow(() -> new IllegalStateException("존재 하지 않는 store table id 입니다."));

        if (storeTable.isTableEmpty()) {
            // return true;
        }
        // return false;
    }

    public void orderOrderMenuByTableOrderId(Long tableOrderId) {
        TableOrder tableOrder = tableOrderRepository.findById(tableOrderId)
                .orElseThrow(() -> new IllegalStateException("존재 하지 않는 table order id 입니다."));
        // 메뉴들 받아서 TableOrderMenu 만들고 추가

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
