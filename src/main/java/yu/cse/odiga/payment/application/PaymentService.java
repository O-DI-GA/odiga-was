package yu.cse.odiga.payment.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.history.application.UseHistoryService;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.TableOrder;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TableOrderRepository tableOrderRepository;
    private final UserRepository userRepository;
    private final UseHistoryService useHistoryService;

    @Transactional
    public void payInUserApp(Long tableOrderId, CustomUserDetails customUserDetails) {
        // TODO : Order id -> 주문 정보 가져와서 그걸로 결제 -> 완료시 사용 내역에 저장
        TableOrder tableOrder = tableOrderRepository.findById(tableOrderId).orElseThrow();
        // TODO : 추후 실제 결제 로직 구현 필요
        tableOrder.completeOrder();
        User user = userRepository.findByEmail(customUserDetails.getUsername()).orElseThrow();
        useHistoryService.saveUseHistory(user, tableOrder);
    }
}

//POST api/v1/user/payments?orderId=1&payType=toss