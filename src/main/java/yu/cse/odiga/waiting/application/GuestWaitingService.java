package yu.cse.odiga.waiting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.dto.WaitingValidateDto;

@Service
@RequiredArgsConstructor
public class GuestWaitingService {

    final WaitingRepository waitingRepository;

    void waitingValidate(WaitingValidateDto waitingValidateDto) {
        Waiting waiting = waitingRepository.findByWaitingCode(waitingValidateDto.getWaitingCode()).orElseThrow();
        waiting.changeWaitingStatusToComplete();
    }
}
