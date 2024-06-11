package yu.cse.odiga.waiting.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitingMenuDto {
    private Long menuId;
    private int menuCount;
}
