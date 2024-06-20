package yu.cse.odiga.waiting.controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.waiting.application.GuestWaitingService;
import yu.cse.odiga.waiting.dto.WaitingValidateDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/tablet/store/{storeId}/waiting")
public class GuestWaitingController {

    private final GuestWaitingService guestWaitingService;

    @PostMapping
    public ResponseEntity<?> enterWaitingGuest(@PathVariable Long storeId,
                                               @RequestBody WaitingValidateDto waitingValidateDto) {

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "Validate Waiting code success",
                guestWaitingService.waitingValidate(waitingValidateDto, storeId)));
    }

}
