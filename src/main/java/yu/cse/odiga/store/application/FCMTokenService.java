package yu.cse.odiga.store.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.FCMTokenRequestDto;

@Service
@RequiredArgsConstructor
public class FCMTokenService {

	private final StoreRepository storeRepository;

	@Transactional
	public void updateFcmToken(FCMTokenRequestDto fcmTokenRequestDto) {
		Store store = storeRepository.findById(fcmTokenRequestDto.getStoreId())
			.orElseThrow(() -> new BusinessLogicException("존재하지 않는 가게 입니다.", HttpStatus.BAD_REQUEST.value()));

		store.setPosDeviceFcmToken(fcmTokenRequestDto.getFcmToken());
	}

}
