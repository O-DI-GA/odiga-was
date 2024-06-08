package yu.cse.odiga.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.store.domain.Category;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDto {
    private String menuName;
    private int price;
    private String caption;
    private String menuImage;
    private Category category;
}
