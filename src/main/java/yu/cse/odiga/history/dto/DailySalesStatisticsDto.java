package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailySalesStatisticsDto {
    private Long storeId;           // 매장 ID
    private Long totalSalesCount;   // 총 판매량
    private Long totalSalesAmount;  // 총 매출 금액
}
