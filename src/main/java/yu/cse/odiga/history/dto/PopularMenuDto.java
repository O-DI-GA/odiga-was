package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PopularMenuDto {
    private String menuName;    // 인기 메뉴 이름
    private Long menuCount;     // 인기 메뉴 판매량
}
