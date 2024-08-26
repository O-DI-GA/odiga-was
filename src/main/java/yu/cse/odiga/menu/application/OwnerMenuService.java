package yu.cse.odiga.menu.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.menu.dao.CategoryRepository;
import yu.cse.odiga.menu.dao.MenuRepository;
import yu.cse.odiga.menu.domain.Category;
import yu.cse.odiga.menu.domain.Menu;
import yu.cse.odiga.menu.dto.CategoryDto;
import yu.cse.odiga.menu.dto.MenuRegisterDto;
import yu.cse.odiga.menu.dto.MenuResponseDto;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

@Service
@RequiredArgsConstructor
public class OwnerMenuService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final MenuImageService menuImageService;

    public void categoryRegister(OwnerUserDetails ownerUserDetails, Long storeId, CategoryDto categoryDto) {

        Store store = getValidateStore(storeId, ownerUserDetails.getOwner().getId());

        Category category = Category.builder()
                .name(categoryDto.getName())
                .store(store)
                .build();

        categoryRepository.save(category);
    }

    public void menuRegister(OwnerUserDetails ownerUserDetails, Long storeId, Long categoryId,
                             MenuRegisterDto menuRegisterDto) throws IOException {

        getValidateStore(storeId, ownerUserDetails.getOwner().getId());

        Category category = categoryRepository.findByStoreIdAndId(storeId, categoryId)
                .orElseThrow(() -> new IllegalArgumentException(("Invalid categoryId: " + categoryId)));

        Menu menu = Menu.builder()
                .menuName(menuRegisterDto.getMenuName())
                .price(menuRegisterDto.getPrice())
                .caption(menuRegisterDto.getCaption())
                .menuImageUrl(menuImageService.upload(menuRegisterDto.getMenuImage()))
                .category(category)
                .build();

        menuRepository.save(menu);

    }

    public List<CategoryDto> findAllCategoryByStoreId(OwnerUserDetails ownerUserDetails, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("존재하지 않는 가게 입니다. : " + storeId, HttpStatus.BAD_REQUEST.value()));

        if (store.isNotStoreOwner(ownerUserDetails.getOwner().getId())) {
            throw new BusinessLogicException("올바르지 않은 접근 입니다.", HttpStatus.BAD_REQUEST.value());
        }

        List<Category> categories = store.getCategories();
        List<CategoryDto> responseCategories = new ArrayList<>();

        for (Category category : categories) {
            CategoryDto categoryDto = CategoryDto.builder()
                    .categoryId((category.getId()))
                    .name(category.getName())
                    .build();

            responseCategories.add(categoryDto);
        }

        return responseCategories;
    }

    public List<MenuResponseDto> findMenu(OwnerUserDetails ownerUserDetails, Long storeId, Long categoryId) {

        getValidateStore(storeId, ownerUserDetails.getOwner().getId());

        Category category = categoryRepository.findByStoreIdAndId(storeId, categoryId)
                .orElseThrow(() -> new IllegalArgumentException(("Invalid categoryId: " + categoryId)));

        List<Menu> storeMenus = category.getMenus();
        List<MenuResponseDto> responseMenus = new ArrayList<>();

        for (Menu menu : storeMenus) {
            MenuResponseDto menuResponseDto = MenuResponseDto.builder()
                    .menuName(menu.getMenuName())
                    .price(menu.getPrice())
                    .caption(menu.getCaption())
                    .menuImage(menu.getMenuImageUrl())
                    .category(menu.getCategory())
                    .build();

            responseMenus.add(menuResponseDto);
        }

        return responseMenus;
    }

    public Store getValidateStore(Long storeId, Long ownerId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게 입니다. :  " + storeId));

        if (store.isNotStoreOwner(ownerId)) {
            throw new IllegalArgumentException("올바르지 않은 접근 입니다.");
        }

        return store;
    }

}
