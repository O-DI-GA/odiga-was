package yu.cse.odiga.waiting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.waiting.application.OwnerWaitingService;
import yu.cse.odiga.waiting.application.UserWaitingService;

@RestController
@RequestMapping("api/v1/owner/waiting")
@RequiredArgsConstructor
public class OwnerWaitingController {

    private final OwnerWaitingService ownerWaitingService;

    @GetMapping("{storeId}")
    public ResponseEntity<?> getStoreWaitingList(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails, @PathVariable Long storeId) {

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "store waiting list",
                ownerWaitingService.findStoreWaitings(ownerUserDetails, storeId)));
    }

    @GetMapping("waitingMenuList/{waitingId}")
    public ResponseEntity<?> getWaitingMenuList(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails, @PathVariable Long waitingId) {

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "waiting menu list",
                ownerWaitingService.getWaitingMenuList(ownerUserDetails, waitingId)));
    }
}
