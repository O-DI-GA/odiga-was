package yu.cse.odiga.history.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.history.application.UserUseHistoryService;

@RestController
@RequestMapping("api/v1/user/history")
@RequiredArgsConstructor
public class UserUseHistoryController {

	private final UserUseHistoryService useHistoryService;

	@GetMapping
	public ResponseEntity<?> getAllUserUseHistory(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return ResponseEntity.ok()
			.body(new DefaultResponse<>(HttpStatus.OK.value(), "이용 내역 조회가 완료되었습니다.",
										useHistoryService.getUserUseHistory(customUserDetails)));
	}

	public ResponseEntity<?> getUserUseHistory(@PathVariable Long historyId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return ResponseEntity.ok()
			.body(new DefaultResponse<>(HttpStatus.OK.value(), "이용 내역 상세 조회가 완료되었습니다.",
										useHistoryService.getUserUserHistoryByHistoryId(historyId, customUserDetails)));
	}

}
