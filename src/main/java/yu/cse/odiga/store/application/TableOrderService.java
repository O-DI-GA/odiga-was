package yu.cse.odiga.store.application;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yu.cse.odiga.global.exception.BusinessLogicException;
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
import yu.cse.odiga.store.type.TableStatus;

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
                .orElseThrow(() -> new BusinessLogicException("존재하지 않는 store table id 입니다.", HttpStatus.BAD_REQUEST.value()));

        List<TableOrderMenuforRegister> requestMenus = tableOrderRegisterDto.getTableOrderMenuforRegisters();

        if (storeTable.isTableEmpty()) {
            storeTable.changeTableStatusToInUse();
            storeTable.setTableOrderList(new ArrayList<>());
        }

        TableOrder currentTableOrder = storeTable.getTableOrderList().stream()
                .filter(order -> order.getPaymentStatus() == PaymentStatus.PENDING)
                .findFirst()
                .orElseGet(() -> {
                    TableOrder newTableOrder = TableOrder.builder()
                            .paymentStatus(PaymentStatus.PENDING)
                            .storeTable(storeTable)
                            .tableOrderMenuList(new ArrayList<>())
                            .build();
                    storeTable.addNewTableOrder(newTableOrder);
                    tableOrderRepository.save(newTableOrder);
                    return newTableOrder;
                });

        for (TableOrderMenuforRegister menuDto : requestMenus) {
            Menu menu = menuRepository.findByCategory_Store_IdAndMenuName(storeId, menuDto.getMenuName())
                    .orElseThrow(() -> new BusinessLogicException("존재하지 않는 메뉴입니다: " + menuDto.getMenuName(), HttpStatus.BAD_REQUEST.value()));

            TableOrderMenu existingTableOrderMenu = currentTableOrder.getTableOrderMenuList().stream()
                    .filter(orderMenu -> orderMenu.getMenu().equals(menu))
                    .findFirst()
                    .orElse(null);

            if (existingTableOrderMenu != null) {
                existingTableOrderMenu.setMenuCount(existingTableOrderMenu.getMenuCount() + menuDto.getMenuCount());
            } else {
                TableOrderMenu newTableOrderMenu = TableOrderMenu.builder()
                        .menu(menu)
                        .menuCount(menuDto.getMenuCount())
                        .tableOrder(currentTableOrder)
                        .build();
                currentTableOrder.getTableOrderMenuList().add(newTableOrderMenu);
                tableOrderMenuRepository.save(newTableOrderMenu);
            }
        }

        storeTableRepository.save(storeTable);

    }

    // TODO : table order id return 필요
    public void checkEmptyTableByStoreTableId(Long storeTableId) {
        StoreTable storeTable = storeTableRepository.findById(storeTableId)
                .orElseThrow(() -> new BusinessLogicException("존재 하지 않는 store table id 입니다.", HttpStatus.BAD_REQUEST.value()));

        if (storeTable.isTableEmpty()) {
            // return true;
        }
        // return false;
    }

    public void orderMenuByTableOrderId(Long tableOrderId) {
        TableOrder tableOrder = tableOrderRepository.findById(tableOrderId)
                .orElseThrow(() -> new BusinessLogicException("존재 하지 않는 table order id 입니다.", HttpStatus.BAD_REQUEST.value()));
        // 메뉴들 받아서 TableOrderMenu 만들고 추가

    }

    public TableOrderHistoryDto getTableOrderList(Long storeId, int storeTableNumber) {
        StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumber(storeId, storeTableNumber)
                .orElseThrow(() -> new BusinessLogicException("해당 테이블이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()));

        if (storeTable.getTableStatus() != TableStatus.INUSE) {
            throw new BusinessLogicException("테이블에 손님이 없습니다.", HttpStatus.BAD_REQUEST.value());
        }

        List<TableOrder> tableOrders = storeTable.getTableOrderList();

        if (tableOrders.isEmpty()) {
            throw new BusinessLogicException("해당 테이블에 주문 내역이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }

        return TableOrderHistoryDto.from(tableOrders);
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
