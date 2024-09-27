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
import yu.cse.odiga.reservation.dto.*;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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

        Map<Integer, Set<LocalDateTime>> existingTimesMap = new HashMap<>();

        for (AvailableReservationTimeDto.MonthSchedule monthSchedule : availableReservationTimeDto.getSchedules()) {
            int month = monthSchedule.getMonth();
            int year = monthSchedule.getYear();
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            if (!existingTimesMap.containsKey(month)) {
                LocalDateTime monthStartDateTime = startDate.atStartOfDay();
                LocalDateTime monthEndDateTime = endDate.atTime(23, 59, 59);

                List<AvailableReservationTime> existingTimes = availableReservationTimeRepository.findByStoreIdAndAvailableReservationTimeBetween(
                        storeId, monthStartDateTime, monthEndDateTime);

                Set<LocalDateTime> existingTimeSet = existingTimes.stream()
                        .map(AvailableReservationTime::getAvailableReservationTime)
                        .collect(Collectors.toSet());

                existingTimesMap.put(month, existingTimeSet);
            }

            Set<LocalDateTime> existingTimeSet = existingTimesMap.get(month);

            for (AvailableReservationTimeDto.DaySchedule schedule : monthSchedule.getDaySchedules()) {
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(schedule.getDayOfWeek().toUpperCase());
                LocalTime startTime = schedule.getStartTime();
                LocalTime endTime = schedule.getEndTime();
                int intervalMinutes = schedule.getIntervalMinutes();

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    if (date.getDayOfWeek() == dayOfWeek) {
                        LocalDateTime slotStartTime = date.atTime(startTime);
                        LocalDateTime slotEndTime = date.atTime(endTime);

//                        // Adjust slotStartTime to be in the future if needed
//                        if (slotStartTime.isBefore(LocalDateTime.now())) {
//                            continue; // 과거 시간은 스킵
//                        }

                        while (!slotStartTime.isAfter(slotEndTime)) {
                            if (!existingTimeSet.contains(slotStartTime)) {
                                AvailableReservationTime availableReservationTime = AvailableReservationTime.builder()
                                        .availableReservationTime(slotStartTime)
                                        .isAvailable(true)
                                        .store(store)
                                        .build();
                                availableReservationTimeList.add(availableReservationTime);
                            }
                            slotStartTime = slotStartTime.plusMinutes(intervalMinutes);
                        }
                    }
                }
            }
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
    public void updateAvailableReservationForDay(OwnerUserDetails ownerUserDetails, Long storeId, AvailableReservationTimeUpdateDto availableReservationTimeUpdateDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("Invalid store ID: " + storeId, HttpStatus.BAD_REQUEST.value()));

        LocalDateTime startOfDay = availableReservationTimeUpdateDto.getDate().atStartOfDay();
        LocalDateTime endOfDay = availableReservationTimeUpdateDto.getDate().atTime(LocalTime.MAX);

        List<AvailableReservationTime> availableReservationTimeList = availableReservationTimeRepository.findByStoreIdAndAvailableReservationTimeBetween(storeId, startOfDay, endOfDay);

        System.out.println(startOfDay);
        System.out.println(endOfDay);
        System.out.println(availableReservationTimeList);

        if (availableReservationTimeList.isEmpty()) {
            throw new BusinessLogicException("No available reservation times found for the specified date", HttpStatus.NOT_FOUND.value());
        }

        // Delete the existing reservation times for that day
        availableReservationTimeRepository.deleteAll(availableReservationTimeList);

        // Recreate new time slots based on the new interval and time range
        LocalDateTime startTime = availableReservationTimeUpdateDto.getDate().atTime(availableReservationTimeUpdateDto.getNewStartTime());
        LocalDateTime endTime = availableReservationTimeUpdateDto.getDate().atTime(availableReservationTimeUpdateDto.getNewEndTime());

        List<AvailableReservationTime> updatedAvailableReservationTimeList = new ArrayList<>();

        while (startTime.isBefore(endTime) || startTime.equals(endTime)) {
            AvailableReservationTime availableReservationTime = AvailableReservationTime.builder()
                    .availableReservationTime(startTime)
                    .isAvailable(availableReservationTimeUpdateDto.isAvailable())
                    .store(store)
                    .build();
            updatedAvailableReservationTimeList.add(availableReservationTime);
            startTime = startTime.plusMinutes(availableReservationTimeUpdateDto.getIntervalMinutes());
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