package yu.cse.odiga.history.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.reservation.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationHistoryRepository extends JpaRepository<Reservation, Long> {
    Optional<List<Reservation>> findByStoreId(Long storeId);
}
