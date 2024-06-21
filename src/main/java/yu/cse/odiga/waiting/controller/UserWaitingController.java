package yu.cse.odiga.waiting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.waiting.application.UserWaitingService;
import yu.cse.odiga.waiting.dto.WaitingRegisterDto;

@RestController
@RequestMapping("api/v1/user/waiting")
@RequiredArgsConstructor
public class UserWaitingController {

    private final UserWaitingService userWaitingService;

    @PostMapping("{storeId}")
    public ResponseEntity<?> registerWaiting(@PathVariable Long storeId,
                                             @RequestBody WaitingRegisterDto waitingRegisterDto,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "register waiting",
                userWaitingService.registerWaiting(storeId, waitingRegisterDto, customUserDetails)));
    }

    @DeleteMapping("{waitingId}")
    public ResponseEntity<?> unregisterWaiting(@PathVariable Long waitingId) {
        userWaitingService.unregisterWaiting(waitingId);

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "unregister waiting", null));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyWaitingList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "user waiting list",
                userWaitingService.findUserWaitings(customUserDetails)));
    }

    @GetMapping("/my/{waitingId}")
    public ResponseEntity<?> getUserWaitingDetail(@PathVariable Long waitingId,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "user waiting detail",
                userWaitingService.userWaitingDetail(waitingId)));

    }
}
