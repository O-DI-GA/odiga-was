package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponseDto {
    private String name;
    private Long totalSalesCount;
    private Long totalSalesAmount;
}
