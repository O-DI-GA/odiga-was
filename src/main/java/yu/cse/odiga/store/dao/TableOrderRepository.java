package yu.cse.odiga.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.store.domain.TableOrder;

public interface TableOrderRepository extends JpaRepository<TableOrder, Long> {
}
