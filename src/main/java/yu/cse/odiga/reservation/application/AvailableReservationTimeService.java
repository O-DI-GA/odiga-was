package yu.cse.odiga.reservation.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.reservation.dao.AvailableReservationTimeRepository;
import yu.cse.odiga.reservation.domain.AvailableReservationTime;
import yu.cse.odiga.reservation.dto.AvailableReservationTimeDto;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableReservationTimeService {
    private final AvailableReservationTimeRepository availableReservationTimeRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void registerAvailableReservationTime(OwnerUserDetails ownerUserDetails, Long storeId, List<AvailableReservationTimeDto> availableReservationTimeDtoList) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid store ID: " + storeId, HttpStatus.BAD_REQUEST.value()));

        List<AvailableReservationTime> availableReservationTimeList = new ArrayList<>();

        for (AvailableReservationTimeDto availableReservationTimeDto : availableReservationTimeDtoList) {
            AvailableReservationTime availableReservationTime = AvailableReservationTime.builder()
                    .availableReservationTime(availableReservationTimeDto.getTime())
                    .isAvailable(true)
                    .store(store)
                    .build();
            availableReservationTimeList.add(availableReservationTime);
        }

        store.setAvailableReservationTimeList(availableReservationTimeList);
        availableReservationTimeRepository.saveAll(availableReservationTimeList);
    }


    @Transactional
    public void toggleAvailableReservationTime(OwnerUserDetails ownerUserDetails, Long availableReservationTimeId) {
        AvailableReservationTime availableReservationTime = availableReservationTimeRepository.findById(availableReservationTimeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid availableReservationTime ID: " + availableReservationTimeId, HttpStatus.BAD_REQUEST.value()));

        availableReservationTime.setAvailable(!availableReservationTime.isAvailable());
    }
}