package yu.cse.odiga.store.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TableOrderMenuListDto {
    private String menuName;
    private int menuCount;
    private int menuTotalPrice;
}
