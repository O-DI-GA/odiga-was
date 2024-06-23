package yu.cse.odiga.waiting.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.store.dao.MenuRepository;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Menu;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.waiting.dao.WaitingMenuRepository;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.domain.WaitingMenu;
import yu.cse.odiga.waiting.dto.UserWaitingDetailDto;
import yu.cse.odiga.waiting.dto.UserWaitingDto;
import yu.cse.odiga.waiting.dto.WaitingCodeResponseDto;
import yu.cse.odiga.waiting.dto.WaitingMenuDto;
import yu.cse.odiga.waiting.dto.WaitingRegisterDto;
import yu.cse.odiga.waiting.exception.AlreadyCancelWaitingException;
import yu.cse.odiga.waiting.exception.AlreadyHasWaitingException;
import yu.cse.odiga.waiting.exception.NotFoundWaitingException;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Service
@RequiredArgsConstructor
public class UserWaitingService {
    private static final int RANDOM_CODE_LENGTH = 6;

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final WaitingMenuRepository waitingMenuRepository;

    /**
     * Waiting 등록
     */
    @Transactional
    public WaitingCodeResponseDto registerWaiting(Long storeId, WaitingRegisterDto waitingRegisterDto,
                                                  CustomUserDetails customUserDetails) {

        // TODO : 해당 로직 변경 필요 웨이팅을 재등록하는 case 도 있음
        Optional<Waiting> userWaiting = waitingRepository.findByStoreIdAndUserIdAndWaitingStatus(storeId,
                customUserDetails.getUser().getId(), WaitingStatus.INCOMPLETE);

        if (userWaiting.isPresent()) {
            throw new AlreadyHasWaitingException("이미 웨이팅을 등록한 가게 입니다.");
        }
        // 취소하고 다시 웨이팅 기능 이상함

        Store store = storeRepository.findById(storeId).orElseThrow();
        List<WaitingMenu> waitingMenus = new ArrayList<>();
        List<WaitingMenuDto> requestMenus = waitingRegisterDto.getRegisterMenus();
        String randomWaitingCode = generateRandomCode();

        Waiting waiting = Waiting.builder()
                .user(customUserDetails.getUser())
                .store(store)
                .peopleCount(waitingRegisterDto.getPeopleCount())
                .waitingNumber(store.getWaitingList().size() + 1)
                .waitingCode(randomWaitingCode) // 코드 + 웨이팅 번호 + storeID 로 확인 하면 될듯
                .waitingStatus(WaitingStatus.INCOMPLETE)
                .build();

        waitingRepository.save(waiting);

        for (WaitingMenuDto menuDto : requestMenus) {
            Menu menu = menuRepository.findById(menuDto.getMenuId()).orElseThrow();
            WaitingMenu waitingMenu = WaitingMenu.builder()
                    .menu(menu)
                    .waiting(waiting)
                    .menuCount(menuDto.getMenuCount())
                    .totalPrice(menu.getPrice() * menuDto.getMenuCount())
                    .build();

            waitingMenus.add(waitingMenu);
            waitingMenuRepository.save(waitingMenu);
        }

        waiting.setWaitingMenuList(waitingMenus);

        return WaitingCodeResponseDto.builder()
                .waitingCode(randomWaitingCode)
                .build();
    }


    /**
     * Waiting 취소
     */
    @Transactional
    public void unregisterWaiting(Long waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new NotFoundWaitingException("등록된 웨이팅이 없습니다."));

        if (!waiting.isIncomplete()) {
            throw new AlreadyCancelWaitingException("이미 취소된 웨이팅 입니다");
        }

        waiting.changeWaitingStatusToCancel();
    }

    public List<UserWaitingDto> findUserWaitings(CustomUserDetails customUserDetails) {
        List<Waiting> incompleteUserWaitings = waitingRepository.findByUserIdAndWaitingStatus(
                customUserDetails.getUser().getId(), WaitingStatus.INCOMPLETE);
        List<UserWaitingDto> waitingDtoList = new ArrayList<>();

        for (Waiting waiting : incompleteUserWaitings) {
            if (waiting.isIncomplete()) {
                List<Waiting> storeWaitings = waitingRepository.findByStoreId(waiting.getStore().getId());
                UserWaitingDto userWaitingDto = UserWaitingDto.builder()
                        .waitingId(waiting.getId())
                        .storeId(waiting.getStore().getId())
                        .storeName(waiting.getStore().getStoreName())
                        .previousWaitingCount(getPreviousWaitingCount(storeWaitings, waiting.getWaitingNumber()))
                        .storeTitleImage(waiting.getStore().getStoreTitleImage())
                        .build();
                waitingDtoList.add(userWaitingDto);
            }
        }
        return waitingDtoList;
    }

    public UserWaitingDetailDto userWaitingDetail(Long waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new NotFoundWaitingException("등록된 웨이팅이 없습니다."));
        List<Waiting> storeWaitings = waitingRepository.findByStoreId(waiting.getStore().getId());

        return UserWaitingDetailDto.builder()
                .storeId(waiting.getStore().getId())
                .waitingId(waiting.getId())
                .storeName(waiting.getStore().getStoreName())
                .previousWaitingCount(getPreviousWaitingCount(storeWaitings, waiting.getWaitingNumber()))
                .waitingCode(waiting.getWaitingCode())
                .waitingNumber(waiting.getWaitingNumber())
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

    public String generateRandomCode() {
//        StringBuilder code = new StringBuilder();
//
//        for (int i = 0; i < RANDOM_CODE_LENGTH; i++) {
//
//        }

        return "A12B3C";

        // CODE + new Data(now);
    }

}
