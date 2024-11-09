package yu.cse.odiga.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.menu.domain.Category;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDto {
	private Long menuId;
	private String menuName;
	private int price;
	private String caption;
	private String menuImage;
	private Category category;
}
