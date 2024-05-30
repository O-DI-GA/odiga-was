package yu.cse.odiga.store.application;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.StoreDetailDto;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Service
@RequiredArgsConstructor
public class StoreService {
    final StoreRepository storeRepository;
    final WaitingRepository waitingRepository;
    private static final int EMPTY_COUNT = 0;


    // TODO : store 관련 이미지 기능 merge 후 추가
    // TODO : 찜 관련 이미지 기능 merge 후 추가

    public StoreDetailDto findByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        List<Waiting> waiting = waitingRepository.findByStoreIdAndWaitingStatus(storeId, WaitingStatus.INCOMPLETE);

        int emptyTableCount = Math.max(store.getTableCount() - waiting.size(), EMPTY_COUNT);

        return StoreDetailDto.builder()
                .storeName(store.getStoreName())
                .address(store.getAddress())
                .tableCount(store.getTableCount())
                .emptyTableCount(emptyTableCount)
                .phoneNumber(store.getPhoneNumber())
                .waitingCount(waiting.size())
                .build();
    }

}
