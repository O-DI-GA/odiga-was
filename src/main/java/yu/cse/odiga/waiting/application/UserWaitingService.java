package yu.cse.odiga.waiting.application;


import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.dto.UserWaitingDetailDto;
import yu.cse.odiga.waiting.dto.UserWaitingDto;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Service
@RequiredArgsConstructor
public class UserWaitingService {
    static final int RANDOM_CODE_LENGTH = 6;

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;

    /**
     * Waiting 등록
     */
    @Transactional
    public void registerWaiting(Long storeId, CustomUserDetails customUserDetails) {

        Store store = storeRepository.findById(storeId).orElseThrow();

        Waiting waiting = Waiting.builder()
                .user(customUserDetails.getUser())
                .store(store)
                .waitingNumber(store.getWaitingList().size() + 1)
                .waitingCode(generateRandomCode())
                .waitingStatus(WaitingStatus.INCOMPLETE)
                .build();

        waitingRepository.save(waiting);

    }

    /**
     * Waiting 취소
     */
    @Transactional
    public void unregisterWaiting(Long storeId, CustomUserDetails customUserDetails) {
        Waiting waiting = waitingRepository.findByStoreIdAndUserId(storeId, customUserDetails.getUser().getId())
                .orElseThrow();
        waiting.changeWaitingStatusToComplete();
    }

    public List<UserWaitingDto> findUserWaitings(CustomUserDetails customUserDetails) {
        List<Waiting> userWaitings = waitingRepository.findByUserId(customUserDetails.getUser().getId());
        List<UserWaitingDto> waitingDtoList = new ArrayList<>();

        for (Waiting waiting : userWaitings) {
            if (waiting.isIncomplete()) {
                List<Waiting> storeWaitings = waitingRepository.findByStoreId(waiting.getStore().getId());
                UserWaitingDto userWaitingDto = UserWaitingDto.builder()
                        .waitingId(waiting.getId())
                        .storeId(waiting.getStore().getId())
                        .storeName(waiting.getStore().getStoreName())
                        .previousWaitingCount(getPreviousWaitingCount(storeWaitings, waiting.getWaitingNumber()))
                        .build();
                waitingDtoList.add(userWaitingDto);
            }
        }
        return waitingDtoList;
    }

    public UserWaitingDetailDto userWaitingDetail(Long waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow();
        List<Waiting> storeWaitings = waitingRepository.findByStoreId(waiting.getStore().getId());

        return UserWaitingDetailDto.builder()
                .storeId(waiting.getStore().getId())
                .waitingId(waiting.getId())
                .storeName(waiting.getStore().getStoreName())
                .previousWaitingCount(getPreviousWaitingCount(storeWaitings, waiting.getWaitingNumber()))
                .waitingCode(waiting.getWaitingCode())
                .build();
    }

    public int getPreviousWaitingCount(List<Waiting> waitingList, int waitingCount) {
        int previousWaitingCount = 0;

        if (waitingList.size() == 1) {
            return previousWaitingCount;
        }

        for (int i = 0; i < waitingCount; i++) {
            Waiting waiting = waitingList.get(i);
            if (waiting.isIncomplete()) {
                previousWaitingCount++;
            }
        }
        return previousWaitingCount;
    }

    public int getIncompleteWaitingCount(Long storeId) {
        return waitingRepository.findByStoreIdAndWaitingStatus(storeId, WaitingStatus.INCOMPLETE).size();
    }

    public String generateRandomCode() {
//        StringBuilder code = new StringBuilder();
//
//        for (int i = 0; i < RANDOM_CODE_LENGTH; i++) {
//
//        }

        return "A12B3C";
    }

}
