package yu.cse.odiga.menu.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MenuDto {
    private Long menuId;
    private String menuName;
    private String menuImageUrl;
    private int menuPrice;
}
