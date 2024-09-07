package yu.cse.odiga.reservation.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.reservation.dao.ReservationRepository;
import yu.cse.odiga.reservation.domain.Reservation;
import yu.cse.odiga.reservation.dto.ReservationRegisterDto;
import yu.cse.odiga.reservation.dto.ReservationResponseDto;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    // 예약하기
    public void registerReservation(CustomUserDetails customUserDetails, Long storeId, ReservationRegisterDto reservationRegisterDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));

        User user = customUserDetails.getUser();

        Reservation reservation = Reservation.builder()
                .user(user)
                .store(store)
                .reservationDateTime(reservationRegisterDto.getReservationDateTime())
                .peopleCount(reservationRegisterDto.getPeopleCount())
                .build();

        reservationRepository.save(reservation);
    }

    // 유저의 예약 목록
    public List<ReservationResponseDto> userReservationList(CustomUserDetails customUserDetails) {

//        List<ReservationResponseDto> reservationResponseDtoList = reservationRepository.findByUserEmail(customUserDetails.getUsername()).stream()
//                .map(ReservationResponseDto::from).toList();

        List<Reservation> reservationList = reservationRepository.findByUserEmail(customUserDetails.getUsername());

        List<ReservationResponseDto> reservationResponseDtoList = new ArrayList<>();

        for (Reservation reservation : reservationList) {
            ReservationResponseDto reservationResponseDto = ReservationResponseDto.builder()
                    .user(reservation.getUser())
                    .store(reservation.getStore())
                    .availableReservationDateTime(reservation.getReservationDateTime())
                    .peopleCount(reservation.getPeopleCount())
                    .build();
            reservationResponseDtoList.add(reservationResponseDto);
        }

        return reservationResponseDtoList;
    }

    // 예약 취소하기
    public void deleteReservation(CustomUserDetails customUserDetails, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + reservationId));

        reservationRepository.delete(reservation);
    }

    // 예약 수정하기
    @Transactional
    public void updateReservation(CustomUserDetails customUserDetails, Long reservationId, ReservationRegisterDto reservationRegisterDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("invalid reservation ID: " + reservationId));

        reservation.setPeopleCount(reservationRegisterDto.getPeopleCount());
        reservation.setReservationDateTime(reservationRegisterDto.getReservationDateTime());
    }

}