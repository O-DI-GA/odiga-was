package yu.cse.odiga.history.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.history.domain.HistoryMenu;

public interface HistoryMenuRepository extends JpaRepository<HistoryMenu, Long> {
}
