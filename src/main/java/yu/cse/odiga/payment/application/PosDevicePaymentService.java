package yu.cse.odiga.payment.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.history.application.UseHistoryService;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.TableOrder;

@Service
@RequiredArgsConstructor
public class PosDevicePaymentService {

	private final TableOrderRepository tableOrderRepository;
	private final UseHistoryService useHistoryService;

	@Transactional
	public void payInPosDevice(Long tableOrderId) {
		TableOrder tableOrder = tableOrderRepository.findById(tableOrderId)
			.orElseThrow(() -> new BusinessLogicException("올바른 TableOrder Id 가 아닙니다.", HttpStatus.BAD_REQUEST.value()));
		tableOrder.completeOrder();
		tableOrder.getStoreTable().changeTableStatusToEmpty();
		useHistoryService.saveUseHistoryPayInPosDevice(tableOrder);
	}
}
