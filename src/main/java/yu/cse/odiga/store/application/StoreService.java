package yu.cse.odiga.store.application;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Category;
import yu.cse.odiga.store.domain.Menu;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.MenuDto;
import yu.cse.odiga.store.dto.StoreDetailDto;
import yu.cse.odiga.store.dto.StoreListDto;
import yu.cse.odiga.store.dto.StoreMenuListDto;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private static final int EMPTY_TABLE_COUNT = 0;
    private static final double EMPTY_REVIEW_RATING = 0.0;

    public List<StoreListDto> findAll() {
        List<Store> stores = storeRepository.findAll();
        List<StoreListDto> storeList = new ArrayList<>();
        for (Store s : stores) {
            StoreListDto storeListDto = StoreListDto.builder()
                    .storeId(s.getId())
                    .storeName(s.getStoreName())
                    .storeTitleImage(s.getStoreTitleImage())
                    .build();
            storeList.add(storeListDto);
        }

        return storeList;
    }

    public StoreDetailDto findByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow();
//        List<Waiting> waiting = waitingRepository.findByStoreIdAndWaitingStatus(storeId, WaitingStatus.INCOMPLETE);

        List<Waiting> incompleteWaitings = store.getWaitingList().stream()
                .filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.INCOMPLETE)
                .toList();

//        double averageReviewScore = store.getReviewList().stream().mapToInt(Review::getRating).average()
//                .orElse(EMPTY_REVIVE_RATING);

        int emptyTableCount = Math.max(store.getTableCount() - incompleteWaitings.size(), EMPTY_TABLE_COUNT);

        return StoreDetailDto.builder()
                .storeName(store.getStoreName())
                .storeTitleImage(store.getStoreTitleImage())
                .averageRating(EMPTY_REVIEW_RATING)
                .likeCount(store.getLikeCount())
                .address(store.getAddress())
                .tableCount(store.getTableCount())
                .emptyTableCount(emptyTableCount)
                .phoneNumber(store.getPhoneNumber())
                .waitingCount(incompleteWaitings.size())
                .build();
    }

    public List<StoreMenuListDto> findStoreMenus(Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow();

        List<Category> storeCategoryList = store.getCategories();
        List<StoreMenuListDto> menuListDtoList = new ArrayList<>();

        for (Category category : storeCategoryList) {
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

            menuListDtoList.add(storeMenuListDto);
        }

        return menuListDtoList;
    }
}
