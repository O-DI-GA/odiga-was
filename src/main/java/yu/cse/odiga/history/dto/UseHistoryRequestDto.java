package yu.cse.odiga.history.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.history.domain.HistoryMenu;
import yu.cse.odiga.history.domain.UseHistory;

@Builder
@AllArgsConstructor
@Getter
public class UseHistoryRequestDto {
	private Long historyId;
	private Long storeId;
	private String storeName;
	private int totalPrice;
	private LocalDateTime visitedAt;
	private List<UseHistoryMenuDto> menus;

	public static UseHistoryRequestDto from(UseHistory useHistory) {
		List<UseHistoryMenuDto> menus = useHistory.getHistoryMenus()
			.stream()
			.map(UseHistoryMenuDto::from)
			.collect(Collectors.toList());

		return UseHistoryRequestDto.builder()
			.historyId(useHistory.getId())
			.storeId(useHistory.getStore().getId())
			.storeName(useHistory.getStore().getStoreName())
			.totalPrice(useHistory.getPaymentAmount())
			.visitedAt(useHistory.getCreatedAt())
			.menus(menus)
			.build();
	}

	@Getter
	@Builder
	@AllArgsConstructor
	static class UseHistoryMenuDto {
		private Long menuId;
		private String menuName;
		private int menuPrice;
		private int menuCount;
		private String menuImage;

		private static UseHistoryMenuDto from(HistoryMenu historyMenu) {
			return UseHistoryMenuDto.builder()
				.menuId(historyMenu.getMenu().getId())
				.menuName(historyMenu.getMenu().getMenuName())
				.menuPrice(historyMenu.getMenu().getPrice())
				.menuImage(historyMenu.getMenu().getMenuImageUrl())
				.menuCount(historyMenu.getMenuCount())
				.build();
		}
	}
}
