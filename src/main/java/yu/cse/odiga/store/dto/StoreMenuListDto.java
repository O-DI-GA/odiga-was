package yu.cse.odiga.store.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreMenuListDto {
    private String categoryName;
    private List<MenuDto> menuList;
}
