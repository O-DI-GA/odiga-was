package yu.cse.odiga.history.application;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.history.dao.HistoryMenuRepository;
import yu.cse.odiga.history.domain.HistoryMenu;
import yu.cse.odiga.history.domain.UseHistory;
import yu.cse.odiga.history.dto.*;
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
        return popularMenus.isEmpty() ? null : popularMenus.get(0);
    }

    public List<TopCategoryDto> getTopCategoriesWithMenuRatios(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        List<CategorySalesDto> categorySales = historyMenuRepository.getCategorySalesByStoreIdAndDateRange(storeId, startDate, endDate);

        Map<String, Long> categoryTotals = new HashMap<>();
        Map<String, List<CategorySalesDto>> categoryMenus = new HashMap<>();

        for (CategorySalesDto dto : categorySales) {
            categoryTotals.put(dto.getCategoryName(), categoryTotals.getOrDefault(dto.getCategoryName(), 0L) + dto.getMenuCount());
            categoryMenus.computeIfAbsent(dto.getCategoryName(), k -> new ArrayList<>()).add(dto);
        }

        List<Map.Entry<String, Long>> sortedCategories = categoryTotals.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(2)
                .collect(Collectors.toList());

        List<TopCategoryDto> result = new ArrayList<>();
        int rank = 1;
        for (Map.Entry<String, Long> categoryEntry : sortedCategories) {
            String category = categoryEntry.getKey();
            Long totalCategorySales = categoryEntry.getValue();

            List<CategorySalesDto> menus = categoryMenus.get(category);

            menus.sort((a, b) -> b.getMenuCount().compareTo(a.getMenuCount()));
            List<CategoryMenuDto> topMenus = new ArrayList<>();

            for (int i = 0; i < Math.min(2, menus.size()); i++) {
                CategorySalesDto menuDto = menus.get(i);
                int ratio = (int) ((double) menuDto.getMenuCount() / totalCategorySales * 100);
                topMenus.add(CategoryMenuDto.builder()
                        .menuName(menuDto.getMenuName())
                        .totalSalesAmount(menuDto.getMenuCount())
                        .ratio(ratio)
                        .build());
            }

            result.add(TopCategoryDto.builder()
                    .categoryName(category)
                    .rank(rank++)
                    .menus(topMenus)
                    .build());
        }

        return result;
    }
}
