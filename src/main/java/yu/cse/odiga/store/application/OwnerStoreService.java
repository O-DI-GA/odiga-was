package yu.cse.odiga.store.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.dao.MenuRepository;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Menu;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.MenuRegisterDto;
import yu.cse.odiga.store.dto.MenuResponseDto;
import yu.cse.odiga.store.dto.StoreRegisterDto;
import yu.cse.odiga.store.dto.StoreResponseDto;
import yu.cse.odiga.store.type.Category;

@Service
@RequiredArgsConstructor
public class OwnerStoreService {
    private final StoreRepository storeRepository;
    private final StoreImageService storeImageService;
    private final MenuRepository menuRepository;
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

        if (storeRegisterDto.getStoreImage() != null && storeRegisterDto.getStoreTitleImage() != null && !storeRegisterDto.getStoreImage().isEmpty() && !storeRegisterDto.getStoreTitleImage().isEmpty()) {
            try {
                storeImageService.upload(storeRegisterDto.getStoreTitleImage(), storeRegisterDto.getStoreImage(), store);
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

    public void menuRegister(OwnerUserDetails ownerUserDetails, Long storeId, MenuRegisterDto menuRegisterDto) throws IOException {
        Store store = storeRepository.findByOwnerIdAndId(ownerUserDetails.getOwner().getId(), storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));
        String menuName = menuRegisterDto.getMenuName();
        int price = menuRegisterDto.getPrice();
        String caption = menuRegisterDto.getCaption();
        Category category = Category.valueOf(menuRegisterDto.getCategory());
        String menuImageUrl = menuImageService.upload(menuRegisterDto.getMenuImage());

        Menu menu = Menu.builder()
                .menuName(menuName)
                .price(price)
                .caption(caption)
                .menuImageUrl(menuImageUrl)
                .category(category)
                .store(store)
                .build();

        menuRepository.save(menu);

    }

    public List<MenuResponseDto> findStoreMenu(OwnerUserDetails ownerUserDetails, Long storeId) {

        Store store = storeRepository.findByOwnerIdAndId(ownerUserDetails.getOwner().getId(), storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));
        List<Menu> storeMenus = store.getMenus();
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
