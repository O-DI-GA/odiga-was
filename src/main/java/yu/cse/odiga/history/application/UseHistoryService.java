package yu.cse.odiga.history.application;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.history.dao.UseHistoryRepository;
import yu.cse.odiga.history.domain.HistoryMenu;
import yu.cse.odiga.history.domain.UseHistory;
import yu.cse.odiga.store.domain.TableOrder;

@Service
@RequiredArgsConstructor
public class UseHistoryService {
    private final UseHistoryRepository useHistoryRepository;
    private final HistoryMenuService historyMenuService;

    public void saveUseHistory(User user, TableOrder tableOrder) {
        UseHistory useHistory = UseHistory.builder()
                .user(user)
                .paymentAmount(tableOrder.getTableTotalPrice())
                .store(tableOrder.getStore())
                .build();

        List<HistoryMenu> historyMenus = historyMenuService.tableOrderMenusToHistoryMenus(useHistory, tableOrder.getTableOrderMenuList());

        useHistory.setHistoryMenus(historyMenus);
        useHistoryRepository.save(useHistory);
    }

    public void saveUseHistoryPayInPosDevice(TableOrder tableOrder) {
        UseHistory useHistory = UseHistory.builder()
            .paymentAmount(tableOrder.getTableTotalPrice())
            .store(tableOrder.getStore())
            .build();

        List<HistoryMenu> historyMenus = historyMenuService.tableOrderMenusToHistoryMenus(useHistory, tableOrder.getTableOrderMenuList());
        useHistory.setHistoryMenus(historyMenus);
        useHistoryRepository.save(useHistory);
    }

    public void findAllUserHistoryByUserId(Long userId) {
        useHistoryRepository.findByUserId(userId);
        // TODO : DTO 변환
    }

    public void findAllStoreUseHistoryByStoreId(Long storeId) {

    }
}
