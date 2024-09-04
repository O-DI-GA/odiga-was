package yu.cse.odiga.reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.reservation.domain.AvailableReservationTime;

import java.util.List;

@Repository
public interface AvailableReservationTimeRepository extends JpaRepository<AvailableReservationTime, Long> {

    List<AvailableReservationTime> findByStoreId(Long storeId);

}
