package yu.cse.odiga.store.application;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.store.dao.LikeStoreRepository;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.LikeStore;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.LikeResponseDto;

@Service
@RequiredArgsConstructor
public class LikeStoreService {

    private final LikeStoreRepository likeStoreRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Store add(Long storeId, CustomUserDetails customUserDetails) throws Exception {

        User user = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Could not found user email : " + customUserDetails.getUsername()));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Could not found store id : " + storeId));

        // 이미 좋아요되어있으면 에러 반환
        if (likeStoreRepository.findByUserAndStore(user, store).isPresent()){
            throw new Exception();
        }

        LikeStore likeStore = LikeStore.builder()
                .user(user)
                .store(store)
                .build();

        likeStoreRepository.save(likeStore);

        store.setLikeCount(store.getLikeCount()+ 1);

        return store;
    }

    @Transactional
    public Store delete(Long storeId, CustomUserDetails customUserDetails) {

        User user = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Could not found user email : " + customUserDetails.getUsername()));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Could not found store id : " + storeId));

        LikeStore likeStore = likeStoreRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new NotFoundException("Could not found like id"));

        likeStoreRepository.delete(likeStore);

        store.setLikeCount(store.getLikeCount()-1);

        return store;
    }

    public List<LikeResponseDto> findUserLikedStores(CustomUserDetails customUserDetails) {
        List<LikeStore> likeStores = likeStoreRepository.findByUserId(customUserDetails.getUser().getId());
        List<LikeResponseDto> responseLikes = new ArrayList<>();

        for (LikeStore likeStore : likeStores) {
            Store store = likeStore.getStore();
            LikeResponseDto likeResponseDto = LikeResponseDto.builder()
                    .storeId(store.getId())
                    .storeName(store.getStoreName())
                    .address(store.getAddress())
                    .phoneNumber(store.getPhoneNumber())
                    .storeCategory(store.getStoreCategory())
                    .storeTitleImageUrl(store.getStoreTitleImage())
                    .build();
            responseLikes.add(likeResponseDto);
        }

        return responseLikes;
    }
}