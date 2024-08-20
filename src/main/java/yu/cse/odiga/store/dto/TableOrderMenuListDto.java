package yu.cse.odiga.store.dto;


import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.store.domain.TableOrderMenu;

@Builder
@Getter
public class TableOrderMenuListDto {
    private String menuName;
    private int menuCount;
    private int menuTotalPrice;
    private String menuImageUrl;

}
