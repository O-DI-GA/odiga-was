package yu.cse.odiga.history.application;


import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.history.dao.HistoryMenuRepository;
import yu.cse.odiga.history.domain.HistoryMenu;
import yu.cse.odiga.history.domain.UseHistory;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.domain.TableOrderMenu;

@Service
@RequiredArgsConstructor
public class HistoryMenuService {

    private final HistoryMenuRepository historyMenuRepository;

    public List<HistoryMenu> tableOrderMenusToHistoryMenus(UseHistory useHistory, List<TableOrderMenu> orderMenus) {
        List<HistoryMenu> historyMenuList = new ArrayList<>();

        for (TableOrderMenu tableOrderMenu : orderMenus) {
            HistoryMenu historyMenu = HistoryMenu.builder()
                    .menu(tableOrderMenu.getMenu())
                    .menuCount(tableOrderMenu.getMenuCount())
                    .history(useHistory)
                    .build();
            historyMenuRepository.save(historyMenu);
            historyMenuList.add(historyMenu);
        }
        return historyMenuList;
    }
}
