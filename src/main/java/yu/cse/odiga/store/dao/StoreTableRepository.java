package yu.cse.odiga.store.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.type.TableStatus;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {

	Optional<StoreTable> findById(Long storeTableId);

	List<StoreTable> findByStoreIdAndTableStatus(Long storeId, TableStatus tableStatus);

	List<StoreTable> findByStoreId(Long storeId);

	Optional<StoreTable> findByStoreIdAndTableNumber(Long storeId, int tableNumber);

	Optional<StoreTable> findByStoreIdAndTableNumberAndTableStatus(Long storeId, int tableNumber, TableStatus tableStatus);

}
