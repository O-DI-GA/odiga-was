package yu.cse.odiga.menu.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.menu.dao.CategoryRepository;
import yu.cse.odiga.menu.domain.Category;
import yu.cse.odiga.menu.domain.Menu;
import yu.cse.odiga.menu.dto.MenuDto;
import yu.cse.odiga.menu.dto.StoreMenuListDto;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final CategoryRepository categoryRepository;

	public List<StoreMenuListDto> findStoreMenus(Long storeId) {

		List<Category> categories = categoryRepository.findByStoreId(storeId);
		List<StoreMenuListDto> storeMenuListResponse = new ArrayList<>();

		for (Category category : categories) {
			List<Menu> menus = category.getMenus();
			List<MenuDto> menuList = new ArrayList<>();

			for (Menu menu : menus) {
				MenuDto menuDto = MenuDto.builder()
					.menuId(menu.getId())
					.menuName(menu.getMenuName())
					.menuImageUrl(menu.getMenuImageUrl())
					.menuPrice(menu.getPrice())
					.build();

				menuList.add(menuDto);
			}

			StoreMenuListDto storeMenuListDto = StoreMenuListDto.builder()
				.categoryName(category.getName())
				.menuList(menuList)
				.build();

			storeMenuListResponse.add(storeMenuListDto);
		}
		return storeMenuListResponse;
	}
}
