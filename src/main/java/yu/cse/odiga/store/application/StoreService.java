package yu.cse.odiga.store.application;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.store.dao.StoreImageRepository;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreImage;
import yu.cse.odiga.store.dto.StoreDetailDto;
import yu.cse.odiga.store.dto.StoreImagesDto;
import yu.cse.odiga.store.dto.StoreListDto;
import yu.cse.odiga.store.dto.StoreMapDto;
import yu.cse.odiga.store.type.SortCondition;
import yu.cse.odiga.store.type.TableStatus;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Service
@RequiredArgsConstructor
public class StoreService {
	private final WaitingRepository waitingRepository;
	private final StoreRepository storeRepository;
	private final StoreTableRepository storeTableRepository;
	private final StoreImageRepository storeImageRepository;
	private static final int EMPTY_TABLE_COUNT = 0;
	private static final double EMPTY_REVIEW_RATING = 0.0;
	private static final double RANGE_OF_RADIUS = 0.007; // 약 700m

	public List<StoreMapDto> findAroundStoreInMap(Double latitude, Double longitude) {

		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

		List<Store> stores = storeRepository.findAroundStores(point, RANGE_OF_RADIUS);
		List<StoreMapDto> storeList = new ArrayList<>();
		for (Store s : stores) {
			List<Waiting> incompleteWaitings = s.getWaitingList()
				.stream()
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
				.storeCategory(s.getStoreCategory())
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

		if (SortCondition.STORE_LIKE_COUNT.getValue().equals(orderCondition)) {
			findStoreByDistance = storeRepository.findStoresRangeAndOrderByLikeCount(point,
																					 RANGE_OF_RADIUS);  //반경 700m 거리 안에 있는 가게중에 가장 찜이 많은 가게 10개
		} else if (SortCondition.STORE_REVIEW_COUNT.getValue().equals(orderCondition)) {
			findStoreByDistance = storeRepository.findStoresRangeAndOrderByReviewCount(point,
																					   RANGE_OF_RADIUS);  //반경 700m 거리 안에 있는 가게중에 가장 리뷰가 많은 가게 10개
		} else if (SortCondition.STORE_WAITING_COUNT.getValue().equals(orderCondition)) {
			findStoreByDistance = storeRepository.findStoresRangeAndOrderByWaitingCount(point, RANGE_OF_RADIUS);
		}

		for (Store store : findStoreByDistance) {

			List<Waiting> incompleteWaitings = store.getWaitingList()
				.stream()
				.filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.INCOMPLETE)
				.toList();

			StoreListDto storeListDto = StoreListDto.builder()
				.likeCount(store.getLikeCount())
				.reviewCount(store.getReviewCount())
				.storeTitleImage(store.getStoreTitleImage())
				.waitingCount(incompleteWaitings.size())
				.storeId(store.getId())
				.storeName(store.getStoreName())
				.storeCategory(store.getStoreCategory())
				.build();

			responseStore.add(storeListDto);
		}

		return responseStore;
	}

	public StoreDetailDto findByStoreId(Long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow();
		
		int emptyTableCount = 0;
		int waitingCount = 0;

		List<Waiting> waitingList = waitingRepository.findByStoreIdAndWaitingStatus(storeId, WaitingStatus.INCOMPLETE);

		if (waitingList.isEmpty()) {
			emptyTableCount = storeTableRepository.findByStoreIdAndTableStatus(storeId, TableStatus.EMPTY).size();
		} else {
			waitingCount = waitingList.size();
		}

		int sumReviewsScore = store.getReviewList().stream().mapToInt(r -> r.getRating().getValue()).sum();

		double avgReviewsScore = EMPTY_REVIEW_RATING;

		if (sumReviewsScore != 0) {
			avgReviewsScore = (double)sumReviewsScore / store.getReviewList().size();

		}

		return StoreDetailDto.builder()
			.storeName(store.getStoreName())
			.storeTitleImage(store.getStoreTitleImage())
			.averageRating(avgReviewsScore)
			.likeCount(store.getLikeCount())
			.address(store.getAddress())
			.tableCount(store.getTableCount())
			.storeCategory(store.getStoreCategory())
			.emptyTableCount(emptyTableCount)
			.waitingCount(waitingCount)
			.phoneNumber(store.getPhoneNumber())
			.build();
	}

	public List<StoreImagesDto> findStoreImagesByStoreId(Long storeId) {
		List<StoreImage> storeImages = storeImageRepository.findByStoreId(storeId);
		List<StoreImagesDto> responseStoreImages = new ArrayList<>();

		for (StoreImage si : storeImages) {
			StoreImagesDto storeImagesDto = StoreImagesDto.builder().storeImagesUrl(si.getPostImageUrl()).build();

			responseStoreImages.add(storeImagesDto);
		}

		return responseStoreImages;
	}
}