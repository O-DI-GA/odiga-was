package yu.cse.odiga.store.dto;


import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.store.domain.TableOrderMenu;

@Builder
@Getter
public class TableOrderMenuDto {
    private String menuName;
    private int menuCount;
    private int menuTotalPrice;
    private String menuImageUrl;

    public static TableOrderMenuDto from(TableOrderMenu tableOrderMenu) {
        return TableOrderMenuDto.builder()
                .menuName(tableOrderMenu.getMenu().getMenuName())
                .menuCount(tableOrderMenu.getMenuCount())
                .menuImageUrl(tableOrderMenu.getMenu().getMenuImageUrl())
                .menuTotalPrice(tableOrderMenu.getMenu().getPrice() * tableOrderMenu.getMenuCount())
                .build();
    }
}
