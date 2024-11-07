package yu.cse.odiga.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.history.dao.ReservationHistoryRepository;
import yu.cse.odiga.history.dto.ReservationHistoryDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class    ReservationHistoryService {
    private final ReservationHistoryRepository reservationRepository;

    public Map<String, List<ReservationHistoryDto>> getYearlyReservationCounts(Long storeId) {
        var reservations = reservationRepository.findByStoreId(storeId)
                .orElseThrow(() -> new BusinessLogicException("존재하지 않는 storeId 입니다.", HttpStatus.BAD_REQUEST.value()));

        Map<String, Map<String, Long>> yearlyReservationCounts = reservations.stream()
                .collect(Collectors.groupingBy(
                        reservation -> String.valueOf(reservation.getReservationDateTime().getYear()),
                        Collectors.groupingBy(
                                reservation -> String.valueOf(reservation.getReservationDateTime().getMonthValue()),
                                Collectors.counting()
                        )
                ));

        return yearlyReservationCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .map(monthEntry -> new ReservationHistoryDto(monthEntry.getKey(), monthEntry.getValue()))
                                .collect(Collectors.toList())
                ));
    }
}