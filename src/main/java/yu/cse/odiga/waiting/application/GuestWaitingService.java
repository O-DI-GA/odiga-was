package yu.cse.odiga.waiting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.dto.WaitingValidateDto;
import yu.cse.odiga.waiting.exception.WaitingCodeValidateException;

@Service
@RequiredArgsConstructor
public class GuestWaitingService {

    private final WaitingRepository waitingRepository;

    public void waitingValidate(WaitingValidateDto waitingValidateDto, Long storeId) {
        Waiting waiting = waitingRepository.findByWaitingCodeAndStoreId(waitingValidateDto.getWaitingCode(), storeId)
                .orElseThrow(() -> new WaitingCodeValidateException("웨이팅 코드가 일치 하지 않습니다."));
        waiting.changeWaitingStatusToComplete();
    }
}
