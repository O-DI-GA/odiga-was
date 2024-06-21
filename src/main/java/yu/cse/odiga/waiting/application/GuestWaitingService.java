package yu.cse.odiga.waiting.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.OSUtil;
import org.springframework.stereotype.Service;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.dao.TableOrderMenuRepository;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.domain.TableOrderMenu;
import yu.cse.odiga.store.dto.TableNumberResponseDto;
import yu.cse.odiga.store.type.PaymentStatus;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.domain.WaitingMenu;
import yu.cse.odiga.waiting.dto.WaitingValidateDto;
import yu.cse.odiga.waiting.exception.AlreadyEnterWaitingCodeException;
import yu.cse.odiga.waiting.exception.WaitingCodeValidateException;

@Service
@RequiredArgsConstructor
public class GuestWaitingService {

    private final WaitingRepository waitingRepository;
    private final StoreTableRepository storeTableRepository;
    private final TableOrderRepository tableOrderRepository;
    private final TableOrderMenuRepository tableOrderMenuRepository;

    // TODO : 키오스크에서 결제 안하면 그냥 바로 history 넣어 줘야함 -> 결제 쪽 service 에서 구현해야 할듯
    public TableNumberResponseDto waitingValidate(WaitingValidateDto waitingValidateDto, Long storeId) {
        Waiting waiting = waitingRepository.findByWaitingCodeAndStoreId(waitingValidateDto.getWaitingCode(), storeId)
                .orElseThrow(() -> new WaitingCodeValidateException("웨이팅 코드가 일치 하지 않습니다.")); //이거 한 분기 더 예외 처리 해야하는데

        // TODO : 자신의 웨이팅 순서가 아닐때 예외처리 필요함.

        if (!waiting.isIncomplete()) {
            throw new AlreadyEnterWaitingCodeException("이미 완료된 웨이팅 입니다.");
        }

        List<StoreTable> storeTables = waiting.getStore().getTables();

        HashMap<Integer, Long> emptyTableList = new HashMap<>();

        for (StoreTable storeTable : storeTables) {
            if (storeTable.isTableEmpty() && storeTable.getMaxSeatCount() >= waiting.getPeopleCount()) {
                emptyTableList.put(storeTable.getTableNumber(), storeTable.getId());
            }
        }
        // random 으로 지정한 테이블 넘버
        int tableNumber = getRandomTableNumber(emptyTableList);

        // TODO : 해당하는 메뉴들을 테이블에 넣어 줘야함

        StoreTable storeTable = storeTableRepository.findById(emptyTableList.get(tableNumber)).orElseThrow();

        TableOrder tableOrder = TableOrder.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .storeTable(storeTable)
                .build();

        tableOrderRepository.save(tableOrder);

        List<TableOrderMenu> tableOrderMenuList = new ArrayList<>();

        List<WaitingMenu> waitingMenuList = waiting.getWaitingMenuList();

        for (WaitingMenu waitingMenu : waitingMenuList) {
            TableOrderMenu tableOrderMenu = TableOrderMenu.builder()
                    .tableOrder(tableOrder)
                    .menu(waitingMenu.getMenu())
                    .menuCount(waitingMenu.getMenuCount())
                    .build();

            tableOrderMenuList.add(tableOrderMenu);
        }

        tableOrder.setTableOrderMenuList(tableOrderMenuList);

        tableOrderMenuRepository.saveAll(tableOrderMenuList);

        storeTable.changeTableStatusToInUse();

        waiting.changeWaitingStatusToComplete();

        return TableNumberResponseDto.builder()
                .tableNumber(tableNumber)
                .build();
    }

    public int getRandomTableNumber(HashMap<Integer, Long> emptyTableList) {
        Random random = new Random();
        List<Integer> keys = new ArrayList<>(emptyTableList.keySet());
        int randomIndex = random.nextInt(keys.size());
        int tableNumber = keys.get(randomIndex);
        System.out.println(tableNumber);
        return tableNumber;
    }
}
