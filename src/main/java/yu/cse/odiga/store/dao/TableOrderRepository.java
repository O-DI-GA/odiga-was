package yu.cse.odiga.store.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.store.domain.TableOrder;

public interface TableOrderRepository extends JpaRepository<TableOrder, Long> {

    Optional<TableOrder> findByStoreTable_StoreIdAndStoreTable_TableNumber(Long storeId, int tableNumber);

    Optional<TableOrder> findByStoreTableId(Long storeTableId);
}
