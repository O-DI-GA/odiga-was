package yu.cse.odiga.history.application;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.history.dao.HistoryMenuRepository;
import yu.cse.odiga.history.domain.HistoryMenu;
import yu.cse.odiga.history.domain.UseHistory;
import yu.cse.odiga.history.dto.DailySalesStatisticsDto;
import yu.cse.odiga.history.dto.PopularMenuDto;
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

    public List<StatisticsResponseDto> getMenuSalesStatistics(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        return historyMenuRepository.getMenuSalesStatisticsByStoreIdAndDateRange(
                storeId, startDate, endDate);
    }

    public List<StatisticsResponseDto> getCategorySalesStatistics(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        return historyMenuRepository.getCategorySalesStatisticsByStoreIdAndDateRange(
                storeId, startDate, endDate);
    }

    public DailySalesStatisticsDto getTodaySalesStatisticsWithPopularMenu(Long storeId) {
        return historyMenuRepository.getTodaySalesStatistics(storeId);
    }

    public PopularMenuDto getTodayPopularMenu(Long storeId) {
        List<PopularMenuDto> popularMenus = historyMenuRepository.getTodayPopularMenu(storeId);
        return popularMenus.isEmpty() ? null : popularMenus.get(0); // 가장 인기 있는 메뉴 반환
    }
}
