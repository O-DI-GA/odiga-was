package yu.cse.odiga.payment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.payment.application.PaymentService;


@RestController
@RequestMapping("api/v1/user/payments")
@RequiredArgsConstructor
public class UserPaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> pay(@RequestParam Long tableOrderId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        paymentService.payInUserApp(tableOrderId, customUserDetails);
        return ResponseEntity.ok().body(new DefaultResponse<>(200, "성공적으로 결제를 완료 했습니다", null));
    }
}
