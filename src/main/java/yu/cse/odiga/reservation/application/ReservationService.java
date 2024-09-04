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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

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

    public void userReservationList(CustomUserDetails customUserDetails) {
        reservationRepository.findByUserEmail(customUserDetails.getUsername());
    }

    public void deleteReservation(CustomUserDetails customUserDetails, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + reservationId));

        reservationRepository.delete(reservation);
    }

    @Transactional
    public void updateReservation(CustomUserDetails customUserDetails, Long reservationId, ReservationRegisterDto reservationRegisterDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("invalid reservation ID: " + reservationId));

        reservation.setPeopleCount(reservationRegisterDto.getPeopleCount());
        reservation.setReservationDateTime(reservationRegisterDto.getReservationDateTime());
    }

    public List<ReservationResponseDto> findByStoreId(Long storeId) {
        List<ReservationResponseDto> reservationList = reservationRepository.findByStoreId(storeId).stream()
                .map(ReservationResponseDto::from).toList();
        return reservationList;
    }
}