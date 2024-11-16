package yu.cse.odiga.history.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.history.dao.UseHistoryRepository;
import yu.cse.odiga.history.domain.UseHistory;
import yu.cse.odiga.history.dto.UseHistoryRequestDto;

@Service
@RequiredArgsConstructor
public class UserUseHistoryService {
	private final UseHistoryRepository useHistoryRepository;
	private final UserRepository userRepository;

	public List<UseHistoryRequestDto> getUserUseHistory(CustomUserDetails userDetails) {
		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new BusinessLogicException("올바르지 않은 접근입니다.", HttpStatus.BAD_REQUEST.value()));

		List<UseHistory> historyList = useHistoryRepository.findByUserId(user.getId());

		return historyList.stream().map(UseHistoryRequestDto::from).collect(Collectors.toList());
	}

	public UseHistoryRequestDto getUserUserHistoryByHistoryId(Long historyId, CustomUserDetails userDetails) {
		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new BusinessLogicException("올바르지 않은 접근입니다.", HttpStatus.BAD_REQUEST.value()));

		UseHistory useHistory = useHistoryRepository.findById(historyId)
			.orElseThrow(() -> new BusinessLogicException("올바르지 않은 접근입니다.", HttpStatus.BAD_REQUEST.value()));

		if (!useHistory.getUser().equals(user)) {
			throw new BusinessLogicException("올바르지 않은 접근입니다.", HttpStatus.BAD_REQUEST.value());
		}

		return UseHistoryRequestDto.from(useHistory);
	}
}
