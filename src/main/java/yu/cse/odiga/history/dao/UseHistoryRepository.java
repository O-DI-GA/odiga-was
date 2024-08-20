package yu.cse.odiga.history.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.history.domain.UseHistory;

public interface UseHistoryRepository extends JpaRepository<UseHistory, Long> {
    List<UseHistory> findByUserId(Long userId);
}
