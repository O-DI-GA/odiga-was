package yu.cse.odiga.waiting.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.waiting.domain.WaitingMenu;

import java.util.List;

public interface WaitingMenuRepository extends JpaRepository<WaitingMenu, Long> {

    List<WaitingMenu> findByWaitingId(Long waitingId);
}
