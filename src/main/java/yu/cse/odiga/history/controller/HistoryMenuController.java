package yu.cse.odiga.history.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.HistoryMenuService;
import yu.cse.odiga.history.dto.DailySalesStatisticsDto;
import yu.cse.odiga.history.dto.PopularMenuDto;
import yu.cse.odiga.history.dto.StatisticsRequestDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/owner/store/{storeId}/analysis")
@RequiredArgsConstructor
public class HistoryMenuController {

    private final HistoryMenuService historyMenuService;

    @GetMapping("menu-sales-statistics")
    public ResponseEntity<?> getMenuSalesStatistics(@PathVariable Long storeId,
                                                    @RequestBody StatisticsRequestDto statisticsRequestDto) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " store menu-sales-statistics",
                        historyMenuService.getMenuSalesStatistics(storeId, statisticsRequestDto)));
    }

    @GetMapping("category-sales-statistics")
    public ResponseEntity<?> getCategorySalesStatistics(@PathVariable Long storeId,
                                                    @RequestBody StatisticsRequestDto statisticsRequestDto) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " store category-sales-statistics",
                        historyMenuService.getCategorySalesStatistics(storeId, statisticsRequestDto)));
    }
    @GetMapping("today-sales-statistics")
    public ResponseEntity<?> getTodaySalesStatisticsWithPopularMenu(@PathVariable Long storeId) {
        DailySalesStatisticsDto statistics = historyMenuService.getTodaySalesStatisticsWithPopularMenu(storeId);
        return ResponseEntity.ok(new DefaultResponse<>(200, storeId + " store today-sales-statistics", statistics));
    }

    @GetMapping("today-popular-menu")
    public ResponseEntity<?> getTodayPopularMenu(@PathVariable Long storeId) {
        PopularMenuDto popularMenu = historyMenuService.getTodayPopularMenu(storeId);
        return ResponseEntity.ok(new DefaultResponse<>(200, storeId + " store today-popular-menu", popularMenu));
    }
}
