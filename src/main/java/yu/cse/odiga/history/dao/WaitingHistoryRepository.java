package yu.cse.odiga.history.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import yu.cse.odiga.waiting.domain.Waiting;

public interface WaitingHistoryRepository extends JpaRepository<Waiting, Long> {
    Optional<List<Waiting>> findByStoreId(Long storeId);
}