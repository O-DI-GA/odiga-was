package yu.cse.odiga.history.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yu.cse.odiga.history.domain.HistoryMenu;
import yu.cse.odiga.history.dto.CategorySalesDto;
import yu.cse.odiga.history.dto.DailySalesStatisticsDto;
import yu.cse.odiga.history.dto.PopularMenuDto;
import yu.cse.odiga.history.dto.StatisticsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryMenuRepository extends JpaRepository<HistoryMenu, Long> {
    @Query("SELECT new yu.cse.odiga.history.dto.StatisticsResponseDto(mn.menuName, SUM(hm.menuCount), SUM(hm.menuCount * mn.price)) " +
            "FROM HistoryMenu hm " +
            "JOIN UseHistory uh ON hm.history.id = uh.id " +
            "JOIN Menu mn ON hm.menuId = mn.id " +
            "WHERE uh.store.id = :storeId AND uh.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY mn.menuName")
    List<StatisticsResponseDto> getMenuSalesStatisticsByStoreIdAndDateRange(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query("SELECT new yu.cse.odiga.history.dto.StatisticsResponseDto(c.name, SUM(hm.menuCount), SUM(hm.menuCount * mn.price)) " +
            "FROM HistoryMenu hm " +
            "JOIN Menu mn ON hm.menuId = mn.id " +
            "JOIN Category c ON mn.category.id = c.id " +
            "JOIN UseHistory uh ON hm.history.id = uh.id " +
            "WHERE uh.store.id = :storeId AND uh.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY c.name")
    List<StatisticsResponseDto> getCategorySalesStatisticsByStoreIdAndDateRange(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new yu.cse.odiga.history.dto.DailySalesStatisticsDto(uh.store.id, SUM(hm.menuCount), SUM(hm.menuCount * mn.price)) " +
            "FROM HistoryMenu hm " +
            "JOIN Menu mn ON hm.menuId = mn.id " +
            "JOIN UseHistory uh ON hm.history.id = uh.id " +
            "WHERE uh.store.id = :storeId AND CAST(uh.createdAt AS date) = CURRENT_DATE " +
            "GROUP BY uh.store.id")
    DailySalesStatisticsDto getTodaySalesStatistics(@Param("storeId") Long storeId);

    @Query("SELECT new yu.cse.odiga.history.dto.PopularMenuDto(mn.menuName, SUM(hm.menuCount)) " +
            "FROM HistoryMenu hm " +
            "JOIN Menu mn ON hm.menuId = mn.id " +
            "JOIN UseHistory uh ON hm.history.id = uh.id " +
            "WHERE uh.store.id = :storeId AND CAST(uh.createdAt AS date) = CURRENT_DATE " +
            "GROUP BY mn.menuName " +
            "ORDER BY SUM(hm.menuCount) DESC")
    List<PopularMenuDto> getTodayPopularMenu(@Param("storeId") Long storeId);

    @Query("SELECT new yu.cse.odiga.history.dto.CategorySalesDto(c.name, mn.menuName, SUM(hm.menuCount * mn.price)) " +
            "FROM HistoryMenu hm " +
            "JOIN Menu mn ON hm.menuId = mn.id " +
            "JOIN Category c ON mn.category.id = c.id " +
            "JOIN UseHistory uh ON hm.history.id = uh.id " +
            "WHERE uh.store.id = :storeId AND uh.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY c.name, mn.menuName " +
            "ORDER BY c.name, SUM(hm.menuCount) DESC")
    List<CategorySalesDto> getCategorySalesByStoreIdAndDateRange(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
