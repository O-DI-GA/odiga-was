package yu.cse.odiga.reservation.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.reservation.dao.AvailableReservationTimeRepository;
import yu.cse.odiga.reservation.dao.ReservationRepository;
import yu.cse.odiga.reservation.domain.AvailableReservationTime;
import yu.cse.odiga.reservation.domain.Reservation;
import yu.cse.odiga.reservation.dto.AvailableReservationTimeDto;
import yu.cse.odiga.reservation.dto.AvailableReservationTimeResponseDto;
import yu.cse.odiga.reservation.dto.ReservationRegisterDto;
import yu.cse.odiga.reservation.dto.ReservationResponseDto;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerReservationService {
    private final AvailableReservationTimeRepository availableReservationTimeRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    // 예약 가능 시간 등록
    @Transactional
    public void registerAvailableReservationTime(OwnerUserDetails ownerUserDetails, Long storeId, List<AvailableReservationTimeDto> availableReservationTimeDtoList) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid store ID: " + storeId, HttpStatus.BAD_REQUEST.value()));

        List<AvailableReservationTime> availableReservationTimeList = new ArrayList<>();

        for (AvailableReservationTimeDto availableReservationTimeDto : availableReservationTimeDtoList) {
            AvailableReservationTime availableReservationTime = AvailableReservationTime.builder()
                    .availableReservationTime(availableReservationTimeDto.getAvailableReservationTime())
                    .isAvailable(true)
                    .store(store)
                    .build();
            availableReservationTimeList.add(availableReservationTime);
        }

        store.setAvailableReservationTimeList(availableReservationTimeList);
        availableReservationTimeRepository.saveAll(availableReservationTimeList);
    }

    // 예약 가능 시간 목록
    public List<AvailableReservationTimeResponseDto> getAvailableReservationTimes(OwnerUserDetails ownerUserDetails, Long storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid store ID: " + storeId, HttpStatus.BAD_REQUEST.value()));

        List<AvailableReservationTime> availableReservationTimeList = availableReservationTimeRepository.findByStoreId(storeId);

        List<AvailableReservationTimeResponseDto> availableReservationTimeResponseDtoList = new ArrayList<>();

        for(AvailableReservationTime availableReservationTime : availableReservationTimeList) {
            AvailableReservationTimeResponseDto availableReservationTimeResponseDto = AvailableReservationTimeResponseDto.builder()
                    .availableReservationTimeId(availableReservationTime.getId())
                    .storeId(storeId)
                    .availableReservationTime(availableReservationTime.getAvailableReservationTime())
                    .isAvailable(availableReservationTime.isAvailable())
                    .build();

            availableReservationTimeResponseDtoList.add(availableReservationTimeResponseDto);
        }

        return availableReservationTimeResponseDtoList;

    }

    //예약 가능 시간 삭제
    public void deleteAvailableReservationTime(OwnerUserDetails ownerUserDetails, Long availableReservationTimeId) {
        AvailableReservationTime availableReservationTime = availableReservationTimeRepository.findById(availableReservationTimeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid available reservation time: " + availableReservationTimeId, HttpStatus.BAD_REQUEST.value()));

        availableReservationTimeRepository.delete(availableReservationTime);
    }

    // 예약 가능 on/off
    @Transactional
    public void toggleAvailableReservationTime(OwnerUserDetails ownerUserDetails, Long availableReservationTimeId) {
        AvailableReservationTime availableReservationTime = availableReservationTimeRepository.findById(availableReservationTimeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid availableReservationTime ID: " + availableReservationTimeId, HttpStatus.BAD_REQUEST.value()));

        availableReservationTime.setAvailable(!availableReservationTime.isAvailable());
    }

    // 가게 예약 목록
    public List<ReservationResponseDto> findByStoreId(OwnerUserDetails ownerUserDetails, Long storeId) {
        List<ReservationResponseDto> reservationList = reservationRepository.findByStoreId(storeId).stream()
                .map(ReservationResponseDto::from).toList();
        return reservationList;
    }

    // 예약 가능 시간 수정하기
    @Transactional
    public void updateAvailableReservation(OwnerUserDetails ownerUserDetails, Long availableReservationTimeId, AvailableReservationTimeDto availableReservationTimeDto) {
        AvailableReservationTime availableReservationTime = availableReservationTimeRepository.findById(availableReservationTimeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid availableReservationTime ID: " + availableReservationTimeId, HttpStatus.BAD_REQUEST.value()));

        availableReservationTime.setAvailableReservationTime(availableReservationTimeDto.getAvailableReservationTime());
    }

    // 예약 취소하기
    public void deleteReservation(OwnerUserDetails ownerUserDetails, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + reservationId));

        reservationRepository.delete(reservation);
    }

    // 예약 수정하기
    @Transactional
    public void updateReservation(OwnerUserDetails ownerUserDetails, Long reservationId, ReservationRegisterDto reservationRegisterDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("invalid reservation ID: " + reservationId));

        reservation.setPeopleCount(reservationRegisterDto.getPeopleCount());
        reservation.setReservationDateTime(reservationRegisterDto.getReservationDateTime());
    }
}