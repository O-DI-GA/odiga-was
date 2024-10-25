package yu.cse.odiga.store.application;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.menu.dao.MenuRepository;
import yu.cse.odiga.menu.domain.Menu;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.dao.TableOrderMenuRepository;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.domain.TableOrderMenu;
import yu.cse.odiga.store.dto.TableOrderHistoryDto;
import yu.cse.odiga.store.dto.TableOrderMenuHistoryDto;
import yu.cse.odiga.store.dto.TableOrderMenuforRegister;
import yu.cse.odiga.store.dto.TableOrderRegisterDto;
import yu.cse.odiga.store.type.PaymentStatus;

@Service
@RequiredArgsConstructor
public class TableOrderService {
    private final TableOrderRepository tableOrderRepository;
    private final StoreTableRepository storeTableRepository;
    private final TableOrderMenuRepository tableOrderMenuRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void registerTableOrderList(Long storeId, int storeTableNumber, TableOrderRegisterDto tableOrderRegisterDto) {

        StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumber(storeId, storeTableNumber)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 store table id 입니다."));

        List<TableOrderMenuforRegister> requestMenus = tableOrderRegisterDto.getTableOrderMenuforRegisters();

        if (storeTable.isTableEmpty()) {
            storeTable.changeTableStatusToInUse();
            storeTable.setTableOrderList(new ArrayList<>());
        }

        TableOrder newTableOrder = TableOrder.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .storeTable(storeTable)
                .tableOrderMenuList(new ArrayList<>())
                .build();

        storeTable.addNewTableOrder(newTableOrder);
        tableOrderRepository.save(newTableOrder);

        for (TableOrderMenuforRegister menuDto : requestMenus) {
            Menu menu = menuRepository.findByCategory_Store_IdAndMenuName(storeId, menuDto.getMenuName())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 메뉴입니다: " + menuDto.getMenuName()));

            TableOrderMenu tableOrderMenu = TableOrderMenu.builder()
                    .menu(menu)
                    .menuCount(menuDto.getMenuCount())
                    .tableOrder(newTableOrder) // 새로 생성된 주문 내역에 추가
                    .build();

            newTableOrder.getTableOrderMenuList().add(tableOrderMenu);

            tableOrderMenuRepository.save(tableOrderMenu);
        }

        storeTableRepository.save(storeTable);
    }
    // TODO : table order id return 필요
    public void checkEmptyTableByStoreTableId(Long storeTableId) {
        StoreTable storeTable = storeTableRepository.findById(storeTableId)
                .orElseThrow(() -> new IllegalStateException("존재 하지 않는 store table id 입니다."));

        if (storeTable.isTableEmpty()) {
            // return true;
        }
        // return false;
    }

    public void orderMenuByTableOrderId(Long tableOrderId) {
        TableOrder tableOrder = tableOrderRepository.findById(tableOrderId)
                .orElseThrow(() -> new IllegalStateException("존재 하지 않는 table order id 입니다."));
        // 메뉴들 받아서 TableOrderMenu 만들고 추가

    }

    public TableOrderHistoryDto getTableOrderList(Long storeId, int storeTableNumber) {
        StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumber(storeId, storeTableNumber)
                .orElseThrow(() -> new IllegalStateException("해당 테이블이 존재하지 않습니다."));

        List<TableOrder> tableOrders = storeTable.getTableOrderList();

        if (tableOrders.isEmpty()) {
            throw new IllegalStateException("해당 테이블에 주문 내역이 존재하지 않습니다.");
        }

        return TableOrderHistoryDto.from(tableOrders);
    }

    public TableOrderMenuHistoryDto getTableOrderListDetail(Long storeId, int storeTableNumber, Long tableOrderId) {
        TableOrder tableOrder = tableOrderRepository.findByStoreTable_Store_IdAndStoreTable_TableNumberAndId(
                storeId, storeTableNumber, tableOrderId).orElseThrow();

        return TableOrderMenuHistoryDto.from(tableOrder);
    }

    public TableOrderMenuHistoryDto findTableOrderHistoryByTableId(Long storeTableId) {
        // 상태가 결제아직 안한 상태만 불러와야함
        return null;
    }

    public TableOrderMenuHistoryDto findByTableOrderHistoryByTableOrderId(Long tableOrderId) {
        TableOrder tableOrder = tableOrderRepository.findById(tableOrderId).orElseThrow();
        return TableOrderMenuHistoryDto.from(tableOrder);
    }
}
