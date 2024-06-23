package yu.cse.odiga.store.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.type.TableStatus;

import java.util.List;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
    List<StoreTable> findByStoreIdAndTableStatus(Long storeId, TableStatus tableStatus);

    Optional<StoreTable> findByStoreIdAndTableNumber(Long storeId, int tableNumber);

}
