package yu.cse.odiga.reservation.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.reservation.dao.AvailableReservationTimeRepository;
import yu.cse.odiga.reservation.dao.ReservationRepository;
import yu.cse.odiga.reservation.domain.AvailableReservationTime;
import yu.cse.odiga.reservation.domain.Reservation;
import yu.cse.odiga.reservation.dto.*;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public void registerAvailableReservationTime(OwnerUserDetails ownerUserDetails, Long storeId, AvailableReservationTimeDto availableReservationTimeDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid store ID: " + storeId, HttpStatus.BAD_REQUEST.value()));

        List<AvailableReservationTime> availableReservationTimeList = new ArrayList<>();

        // For each month schedule
        for (AvailableReservationTimeDto.MonthSchedule monthSchedule : availableReservationTimeDto.getSchedules()) {
            int month = monthSchedule.getMonth();

            // For each day schedule within the month
            for (AvailableReservationTimeDto.DaySchedule schedule : monthSchedule.getDaySchedules()) {
                LocalDateTime currentTime = getNextDateForMonthAndDayOfWeek(month, schedule.getDayOfWeek(), schedule.getStartTime());
                LocalDateTime endTime = getNextDateForMonthAndDayOfWeek(month, schedule.getDayOfWeek(), schedule.getEndTime());

                // Register time slots in intervals
                while (currentTime.isBefore(endTime) || currentTime.equals(endTime)) {
                    AvailableReservationTime availableReservationTime = AvailableReservationTime.builder()
                            .availableReservationTime(currentTime)
                            .isAvailable(true)
                            .store(store)
                            .build();
                    availableReservationTimeList.add(availableReservationTime);
                    currentTime = currentTime.plusMinutes(schedule.getIntervalMinutes());
                }
            }
        }

        store.setAvailableReservationTimeList(availableReservationTimeList);
        availableReservationTimeRepository.saveAll(availableReservationTimeList);
    }

    // Helper method to get the next date for a specific month and day of the week
    private LocalDateTime getNextDateForMonthAndDayOfWeek(int month, String dayOfWeek, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = now.withMonth(month)
                .with(java.time.DayOfWeek.valueOf(dayOfWeek.toUpperCase()))
                .withHour(time.getHour())
                .withMinute(time.getMinute())
                .withSecond(0)
                .withNano(0);

        // If the calculated date is in the past, move to the next occurrence
        if (date.isBefore(now)) {
            date = date.plusWeeks(1);
        }

        return date;
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
    public void updateAvailableReservationForDay(OwnerUserDetails ownerUserDetails, Long storeId, AvailableReservationTimeUpdateDto availableReservationTimeUpdateDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid store ID: " + storeId, HttpStatus.BAD_REQUEST.value()));

        LocalDateTime startOfDay = availableReservationTimeUpdateDto.getDate().atStartOfDay();
        LocalDateTime endOfDay = availableReservationTimeUpdateDto.getDate().atTime(LocalTime.MAX);

        List<AvailableReservationTime> availableReservationTimeList = availableReservationTimeRepository.findByStoreIdAndAvailableReservationTimeBetween(storeId, startOfDay, endOfDay);

        if (availableReservationTimeList.isEmpty()) {
            throw new BusinessLogicException("No available reservation times found for the specified date", HttpStatus.NOT_FOUND.value());
        }

        // Delete the existing reservation times for that day
        availableReservationTimeRepository.deleteAll(availableReservationTimeList);

        // Recreate new time slots based on the new interval and time range
        LocalDateTime currentTime = availableReservationTimeUpdateDto.getDate().atTime(availableReservationTimeUpdateDto.getNewStartTime());
        LocalDateTime endTime = availableReservationTimeUpdateDto.getDate().atTime(availableReservationTimeUpdateDto.getNewEndTime());

        List<AvailableReservationTime> updatedAvailableReservationTimeList = new ArrayList<>();

        while (currentTime.isBefore(endTime) || currentTime.equals(endTime)) {
            AvailableReservationTime availableReservationTime = AvailableReservationTime.builder()
                    .availableReservationTime(currentTime)
                    .isAvailable(availableReservationTimeUpdateDto.isAvailable())
                    .store(store)
                    .build();
            updatedAvailableReservationTimeList.add(availableReservationTime);
            currentTime = currentTime.plusMinutes(availableReservationTimeUpdateDto.getIntervalMinutes());
        }

        availableReservationTimeRepository.saveAll(updatedAvailableReservationTimeList);
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