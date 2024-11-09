package yu.cse.odiga.history.application;


import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.history.dao.HistoryMenuRepository;
import yu.cse.odiga.history.domain.HistoryMenu;
import yu.cse.odiga.history.domain.UseHistory;
import yu.cse.odiga.history.dto.StatisticsRequestDto;
import yu.cse.odiga.history.dto.StatisticsResponseDto;
import yu.cse.odiga.store.domain.TableOrderMenu;

@Service
@RequiredArgsConstructor
public class HistoryMenuService {

    private final HistoryMenuRepository historyMenuRepository;

    public List<HistoryMenu> tableOrderMenusToHistoryMenus(UseHistory useHistory, List<TableOrderMenu> orderMenus) {
        List<HistoryMenu> historyMenuList = new ArrayList<>();

        for (TableOrderMenu tableOrderMenu : orderMenus) {
            HistoryMenu historyMenu = HistoryMenu.builder()
                    .menuId(tableOrderMenu.getMenu().getId())
                    .menuCount(tableOrderMenu.getMenuCount())
                    .history(useHistory)
                    .build();
            historyMenuRepository.save(historyMenu);
            historyMenuList.add(historyMenu);
        }
        return historyMenuList;
    }

    public List<StatisticsResponseDto> getMenuSalesStatistics(Long storeId, StatisticsRequestDto statisticsRequestDto) {
        return historyMenuRepository.getMenuSalesStatisticsByStoreIdAndDateRange(
                storeId, statisticsRequestDto.getStartDate(), statisticsRequestDto.getEndDate());
    }

    public List<StatisticsResponseDto> getCategorySalesStatistics(Long storeId, StatisticsRequestDto statisticsRequestDto) {
        return historyMenuRepository.getCategorySalesStatisticsByStoreIdAndDateRange(
                storeId, statisticsRequestDto.getStartDate(), statisticsRequestDto.getEndDate());
    }
}
