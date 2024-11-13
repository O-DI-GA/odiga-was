package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TopCategoryDto {
    private String categoryName;
    private int rank;
    private List<CategoryMenuDto> menus;
}