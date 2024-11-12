package yu.cse.odiga.payment.application;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.history.application.UseHistoryService;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.TableOrder;

@Service
@RequiredArgsConstructor
public class UserPaymentService {

	private final TableOrderRepository tableOrderRepository;
	private final UserRepository userRepository;
	private final UseHistoryService useHistoryService;

	@Transactional
	public void payInUserApp(Long tableOrderId, CustomUserDetails customUserDetails) {
		TableOrder tableOrder = tableOrderRepository.findById(tableOrderId).orElseThrow();
		tableOrder.completeOrder();
		tableOrder.getStoreTable().changeTableStatusToEmpty();
		User user = userRepository.findByEmail(customUserDetails.getUsername()).orElseThrow();
		useHistoryService.saveUseHistory(user, tableOrder);
	}
}

//POST api/v1/user/payments?orderId=1&payType=toss