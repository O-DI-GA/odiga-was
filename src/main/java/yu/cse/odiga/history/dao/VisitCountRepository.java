package yu.cse.odiga.history.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.history.domain.VisitCount;
import yu.cse.odiga.store.domain.Store;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitCountRepository extends JpaRepository<VisitCount, Long> {
    Optional<VisitCount> findByStoreAndVisitHourAndDayOfWeekAndCreatedAtBetween(
            Store store, int visitHour, DayOfWeek dayOfWeek, LocalDateTime start, LocalDateTime end);

    List<VisitCount> findByStore_IdAndCreatedAtBetween(Long storeId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<VisitCount> findByStore_Id(Long storeId);
}