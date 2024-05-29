package yu.cse.odiga.auth.application;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.dao.LikeStoreRepository;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.LikeStore;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.auth.dto.LikeStoreDto;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

@Service
@RequiredArgsConstructor
public class LikeStoreService {

    private final LikeStoreRepository likeStoreRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Store like(LikeStoreDto likeStoreDto) throws Exception {

        User user = userRepository.findByEmail(likeStoreDto.getUserEmail())
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + likeStoreDto.getUserEmail()));

        Store store = storeRepository.findByStoreName(likeStoreDto.getStoreName())
                .orElseThrow(() -> new NotFoundException("Could not found board id : " + likeStoreDto.getStoreName()));

        // 이미 좋아요되어있으면 에러 반환
        if (likeStoreRepository.findByUserAndStore(user, store).isPresent()){
            throw new Exception();
        }

        LikeStore likeStore = LikeStore.builder()
                .user(user)
                .store(store)
                .build();

        likeStoreRepository.save(likeStore);

        Integer currentLikeCount = store.getLikeCount();
        store.setLikeCount(currentLikeCount != null ? currentLikeCount + 1 : 1);

        return store;
    }

    @Transactional
    public Store dislike(LikeStoreDto likeStoreDto) {

        User user = userRepository.findByEmail(likeStoreDto.getUserEmail())
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + likeStoreDto.getUserEmail()));

        Store store = storeRepository.findByStoreName(likeStoreDto.getStoreName())
                .orElseThrow(() -> new NotFoundException("Could not found board id : " + likeStoreDto.getStoreName()));

        LikeStore likeStore = likeStoreRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new NotFoundException("Could not found heart id"));

        likeStoreRepository.delete(likeStore);

        store.setLikeCount(store.getLikeCount()-1);

        return store;
    }
}
