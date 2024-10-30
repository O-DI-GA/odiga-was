package yu.cse.odiga.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.FCMTokenService;
import yu.cse.odiga.store.dto.FCMTokenRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/store")
public class FCMTokenController {

	private final FCMTokenService fcmTokenService;

	@PostMapping("fcm")
	public ResponseEntity<?> updateFCMTokenByStoreId(@RequestBody FCMTokenRequestDto fcmTokenRequestDto) {
		fcmTokenService.updateFcmToken(fcmTokenRequestDto);
		return ResponseEntity.ok().body(new DefaultResponse<>(200, "성공적으로 FCM Token을 저장했습니다.", null));
	}
}
