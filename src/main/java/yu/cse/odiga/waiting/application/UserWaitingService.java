package yu.cse.odiga.waiting.application;

import java.util.ArrayList;
import java.util.List;
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

    // TODO : 웨이팅 등록시 메뉴 인원수 추가
    @Transactional
    public WaitingCodeResponseDto registerWaiting(Long storeId, WaitingRegisterDto waitingRegisterDto,
                                                  CustomUserDetails customUserDetails) {

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

        for (WaitingMenuDto menuDto : requestMenus) {
            Menu menu = menuRepository.findById(menuDto.getMenuId()).orElseThrow();
            WaitingMenu waitingMenu = WaitingMenu.builder()
                    .menu(menu)
                    .waiting(waiting)
                    .menuCount(menuDto.getMenuCount())
                    .totalPrice(menu.getPrice() * menuDto.getMenuCount())
                    .build();

            waitingMenus.add(waitingMenu);
        }

        waitingRepository.save(waiting);
        waitingMenuRepository.saveAll(waitingMenus);

        waiting.setWaitingMenuList(waitingMenus);
        //TODO : 웨이팅 인증 코드 return 필요함.

        return WaitingCodeResponseDto.builder()
                .waitingCode(randomWaitingCode)
                .build();
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
        List<Waiting> incompleteUserWaitings = waitingRepository.findByUserIdAndWaitingStatus(customUserDetails.getUser().getId(), WaitingStatus.INCOMPLETE);
        List<UserWaitingDto> waitingDtoList = new ArrayList<>();

        for (Waiting waiting : incompleteUserWaitings) {
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
    }

}
