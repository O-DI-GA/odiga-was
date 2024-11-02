package yu.cse.odiga.waiting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.waiting.dao.WaitingMenuRepository;
import yu.cse.odiga.waiting.dao.WaitingRepository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.domain.WaitingMenu;
import yu.cse.odiga.waiting.dto.OwnerWaitingDto;
import yu.cse.odiga.waiting.dto.WaitingMenuDto;
import yu.cse.odiga.waiting.type.WaitingStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnerWaitingService {

    private final WaitingRepository waitingRepository;
    private final WaitingMenuRepository waitingMenuRepository;
    private final UserRepository userRepository;

    public List<OwnerWaitingDto> findStoreWaitings(OwnerUserDetails ownerUserDetails, Long storeId) {

        List<Waiting> storeWaitings = waitingRepository.findByStoreIdAndWaitingStatus(storeId, WaitingStatus.INCOMPLETE);

        List<OwnerWaitingDto> ownerWaitingDtoList = new ArrayList<>();

        for (Waiting waiting : storeWaitings) {
            User user = userRepository.findById(waiting.getUser().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

            String userName = user.getNickname();

            OwnerWaitingDto ownerWaitingDto = OwnerWaitingDto.builder()
                    .waitingId(waiting.getId())
                    .userName(userName)
                    .waitingNumber(waiting.getWaitingNumber())
                    .peopleCount(waiting.getPeopleCount())
                    .build();

            ownerWaitingDtoList.add(ownerWaitingDto);
        }

        return ownerWaitingDtoList;
    }

    public List<WaitingMenu> getWaitingMenuList(OwnerUserDetails ownerUserDetails, Long waitingId) {
        List<WaitingMenu> waitingMenus = waitingMenuRepository.findByWaitingId(waitingId); //

        List<WaitingMenuDto> waitingMenuDtoList = new ArrayList<>();

        for (WaitingMenu waitingMenu : waitingMenus) {
            WaitingMenuDto waitingMenuDto = WaitingMenuDto.builder()
                    .menuId(waitingMenu.getId())
                    .menuCount(waitingMenu.getMenuCount())
                    .build();

            waitingMenuDtoList.add(waitingMenuDto);
        }
    return waitingMenus;
    }
}
