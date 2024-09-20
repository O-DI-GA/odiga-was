package yu.cse.odiga.store.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.StoreRegisterDto;
import yu.cse.odiga.store.dto.StoreResponseDto;

@Service
@RequiredArgsConstructor
public class OwnerStoreService {
	private final StoreRepository storeRepository;
	private final StoreImageService storeImageService;

	//  TODO : Owner 웹 사이트에서 store 정보를 알 수 있어야함.

	@Transactional
	public void registerStore(OwnerUserDetails ownerUserDetails,
		StoreRegisterDto storeRegisterDto) {

		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point location = geometryFactory.createPoint(
			new Coordinate(storeRegisterDto.getLongitude(), storeRegisterDto.getLatitude()));

		Store store = Store.builder()
			.owner(ownerUserDetails.getOwner())
			.storeName(storeRegisterDto.getStoreName())
			.location(location)
			.phoneNumber(storeRegisterDto.getPhoneNumber())
			.address(storeRegisterDto.getAddress())
			.tableCount(storeRegisterDto.getTableCount())
			.storeCategory(storeRegisterDto.getStoreCategory())
			.build();

		if (storeRegisterDto.getStoreTitleImage() != null && !storeRegisterDto.getStoreTitleImage().isEmpty()) {
			try {
				storeImageService.uploadTitleImage(storeRegisterDto.getStoreTitleImage(), store);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if (storeRegisterDto.getStoreImage() != null && !storeRegisterDto.getStoreImage().isEmpty()) {
			try {
				storeImageService.uploadStoreImage(storeRegisterDto.getStoreImage(), store);
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
				.location(s.getLocation())
				.address(s.getAddress())
				.phoneNumber(s.getPhoneNumber())
				.storeName(s.getStoreName())
				.storeCategory(s.getStoreCategory())
				.reviewCount(s.getReviewCount())
				.build();

			responseStores.add(storeResponseDto);
		}

		return responseStores;
	}

	@Transactional
	public void updateStore(OwnerUserDetails ownerUserDetails, Long storeId, StoreRegisterDto storeRegisterDto) {

		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point location = geometryFactory.createPoint(
				new Coordinate(storeRegisterDto.getLongitude(), storeRegisterDto.getLatitude()));


		Store store = storeRepository.findByOwnerIdAndId(ownerUserDetails.getOwner().getId(), storeId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid storeId: " + storeId));

		if (storeRegisterDto.getStoreTitleImage() != null && !storeRegisterDto.getStoreTitleImage().isEmpty()) {
			try {
				String oldFileName = store.getStoreTitleImage();
				storeImageService.updateTitleImage(storeRegisterDto.getStoreTitleImage(), oldFileName, store);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if (storeRegisterDto.getStoreImage() != null && !storeRegisterDto.getStoreImage().isEmpty()) {
			try {
				storeImageService.updateStoreImage(storeRegisterDto.getStoreImage(), store);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		store.setStoreName(storeRegisterDto.getStoreName());
		store.setPhoneNumber(storeRegisterDto.getPhoneNumber());
		store.setAddress(storeRegisterDto.getAddress());
		store.setTableCount(storeRegisterDto.getTableCount());
		store.setLocation(location);
		store.setStoreCategory(storeRegisterDto.getStoreCategory());

		storeRepository.save(store);
	}
}