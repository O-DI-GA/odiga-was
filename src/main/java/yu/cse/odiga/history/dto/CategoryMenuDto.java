package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMenuDto {
    private String menuName;
    private Long totalSalesAmount;
    private int ratio;
}