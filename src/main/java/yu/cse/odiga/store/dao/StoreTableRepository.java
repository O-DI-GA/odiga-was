package yu.cse.odiga.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.store.domain.StoreTable;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
}
