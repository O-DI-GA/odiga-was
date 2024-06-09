package yu.cse.odiga.store.application;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Category;
import yu.cse.odiga.store.domain.Menu;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.MenuDto;
import yu.cse.odiga.store.dto.StoreDetailDto;
import yu.cse.odiga.store.dto.StoreListDto;
import yu.cse.odiga.store.dto.StoreMapDto;
import yu.cse.odiga.store.dto.StoreMenuListDto;
import yu.cse.odiga.store.type.OrderCondition;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private static final int EMPTY_TABLE_COUNT = 0;
    private static final double EMPTY_REVIEW_RATING = 0.0;
    private static final double RANGE_OF_RADIUS = 0.7;

    public List<StoreMapDto> findAroundStoreInMap(Double latitude, Double longitude) {

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        List<Store> stores = storeRepository.findAroundStores(point, RANGE_OF_RADIUS);
        List<StoreMapDto> storeList = new ArrayList<>();
        for (Store s : stores) {
            List<Waiting> incompleteWaitings = s.getWaitingList().stream()
                    .filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.INCOMPLETE)
                    .toList();

            int emptyTableCount = Math.max(s.getTableCount() - incompleteWaitings.size(), EMPTY_TABLE_COUNT);

            StoreMapDto storeMapDto = StoreMapDto.builder()
                    .storeId(s.getId())
                    .storeName(s.getStoreName())
                    .latitude(s.getLocation().getY())
                    .longitude(s.getLocation().getX())
                    .waitingCount(incompleteWaitings.size())
                    .emptyTableCount(emptyTableCount)
                    .build();

            storeList.add(storeMapDto);
        }

        return storeList;
    }

    public List<StoreListDto> findAroundListStoreList(Double latitude, Double longitude,
                                                      String orderCondition) { // 추구 enum 개선 필요
        List<StoreListDto> responseStore = new ArrayList<>();

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        List<Store> findStoreByDistance = new ArrayList<>();

        if (OrderCondition.STORE_LIKE_COUNT.getValue().equals(orderCondition)) {
            findStoreByDistance = storeRepository.findStoresRangeAndOrderByLikeCount(point,
                    RANGE_OF_RADIUS);  //반경 700m 거리 안에 있는 가게중에 가장 찜이 많은 가게 10개
        } else if (OrderCondition.STORE_REVIEW_COUNT.getValue().equals(orderCondition)) {
            findStoreByDistance = storeRepository.findStoresRangeAndOrderByReviewCount(point,
                    RANGE_OF_RADIUS);  //반경 700m 거리 안에 있는 가게중에 가장 리뷰가 많은 가게 10개
        } else if (OrderCondition.STORE_WAITING_COUNT.getValue().equals(orderCondition)) {
            findStoreByDistance = storeRepository.findStoresRangeAndOrderByWaitingCount(point, RANGE_OF_RADIUS);
        }

        for (Store store : findStoreByDistance) {

            List<Waiting> incompleteWaitings = store.getWaitingList().stream()
                    .filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.INCOMPLETE)
                    .toList();

            StoreListDto storeListDto = StoreListDto.builder()
                    .likeCount(store.getLikeCount())
                    .reviewCount(store.getReviewCount())
                    .storeTitleImage(store.getStoreTitleImage())
                    .waitingCount(incompleteWaitings.size())
                    .storeId(store.getId())
                    .storeName(store.getStoreName())
                    .build();

            responseStore.add(storeListDto);
        }

        return responseStore;
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
