package yu.cse.odiga.store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.type.PaymentStatus;

public interface TableOrderRepository extends JpaRepository<TableOrder, Long> {

	Optional<TableOrder> findByStoreTable_Store_IdAndStoreTable_TableNumberAndId(Long storeId, int tableNumber,
		Long tableOrderId);

	Optional<TableOrder> findByStoreTableIdAndPaymentStatus(Long storeTableId, PaymentStatus paymentStatus);
}
