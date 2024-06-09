package yu.cse.odiga.store.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.dao.CategoryRepository;
import yu.cse.odiga.store.dao.MenuRepository;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Category;
import yu.cse.odiga.store.domain.Menu;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.*;

@Service
@RequiredArgsConstructor
public class OwnerStoreService {
    private final StoreRepository storeRepository;
    private final StoreImageService storeImageService;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final MenuImageService menuImageService;

    @Transactional
    public void storeRegister(OwnerUserDetails ownerUserDetails,
                              StoreRegisterDto storeRegisterDto) {

        Store store = Store.builder()
                .owner(ownerUserDetails.getOwner())
                .storeName(storeRegisterDto.getStoreName())
                .phoneNumber(storeRegisterDto.getPhoneNumber())
                .address(storeRegisterDto.getAddress())
                .tableCount(storeRegisterDto.getTableCount())
                .build();

        if (storeRegisterDto.getStoreImage() != null && storeRegisterDto.getStoreTitleImage() != null
                && !storeRegisterDto.getStoreImage().isEmpty() && !storeRegisterDto.getStoreTitleImage().isEmpty()) {
            try {
                storeImageService.upload(storeRegisterDto.getStoreTitleImage(), storeRegisterDto.getStoreImage(),
                        store);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        storeRepository.save(store);
    }

    public List<StoreResponseDto> findOwnerStore(OwnerUserDetails ownerUserDetails) {

        List<Store> ownerStores = storeRepository.findByOwnerId(ownerUserDetails.getOwner().getId());
        List<StoreResponseDto> responseStores = new ArrayList<>();

        for (Store s : ownerStores) {
            StoreResponseDto storeResponseDto = StoreResponseDto.builder()
                    .storeId(s.getId())
                    .address(s.getAddress())
                    .phoneNumber(s.getPhoneNumber())
                    .storeName(s.getStoreName())
                    .reviewCount(s.getReviewCount())
                    .build();

            responseStores.add(storeResponseDto);
        }

        return responseStores;
    }

    public void categoryRegister(OwnerUserDetails ownerUserDetails, Long storeId, CategoryDto categoryDto) {
        Store store = storeRepository.findByOwnerIdAndId(ownerUserDetails.getOwner().getId(), storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));

        Category category = Category.builder()
                .name(categoryDto.getName())
                .store(store)
                .build();

        categoryRepository.save(category);
    }

    public List<CategoryDto> findCategory(OwnerUserDetails ownerUserDetails, Long storeId) {
        Store store = storeRepository.findByOwnerIdAndId(ownerUserDetails.getOwner().getId(), storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));

        List<Category> categories = store.getCategories();
        List<CategoryDto> responseCategories = new ArrayList<>();

        for (Category category : categories) {
            CategoryDto categoryDto = CategoryDto.builder()
                    .name(category.getName())
                    .build();

            responseCategories.add(categoryDto);
        }

        return responseCategories;
    }


    public void menuRegister(OwnerUserDetails ownerUserDetails, Long storeId, Long categoryId,
                             MenuRegisterDto menuRegisterDto) throws IOException {

        Store store = storeRepository.findByOwnerIdAndId(ownerUserDetails.getOwner().getId(), storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid storeID: " + storeId));

        Category category = categoryRepository.findByStoreIdAndId(storeId, categoryId)
                .orElseThrow(() -> new IllegalArgumentException(("Invalid categoryId: " + categoryId)));

        if (Objects.equals(store.getId(), category.getStore().getId())) {
            String menuName = menuRegisterDto.getMenuName();
            int price = menuRegisterDto.getPrice();
            String caption = menuRegisterDto.getCaption();
            String menuImageUrl = menuImageService.upload(menuRegisterDto.getMenuImage());

            Menu menu = Menu.builder()
                    .menuName(menuName)
                    .price(price)
                    .caption(caption)
                    .menuImageUrl(menuImageUrl)
                    .category(category)
                    .build();

            menuRepository.save(menu);

        } else {
            throw new IllegalArgumentException("");
        }
    }

    public List<MenuResponseDto> findMenu(OwnerUserDetails ownerUserDetails, Long storeId, Long categoryId) {

        Store store = storeRepository.findByOwnerIdAndId(ownerUserDetails.getOwner().getId(), storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));

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


}
