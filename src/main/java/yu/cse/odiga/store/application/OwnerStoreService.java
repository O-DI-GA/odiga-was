package yu.cse.odiga.store.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.StoreRegisterDto;
import yu.cse.odiga.store.dto.StoreResponseDto;

@Service
@RequiredArgsConstructor
public class OwnerStoreService {
    private final StoreRepository storeRepository;


    @Transactional
    public void storeRegister(OwnerUserDetails ownerUserDetails,
                              StoreRegisterDto storeRegisterDto) {

        Store store = Store.builder()
                .owner(ownerUserDetails.getOwner())
                .storeName(storeRegisterDto.getStoreName())
                .phoneNumber(storeRegisterDto.getPhoneNumber())
                .address(storeRegisterDto.getAddress())
                .build();

        storeRepository.save(store);
    }

    public List<StoreResponseDto> findOwnerStore(OwnerUserDetails ownerUserDetails) {

        List<Store> ownerStores = storeRepository.findByOwnerId(ownerUserDetails.getOwner().getId());
        List<StoreResponseDto> responseStores = new ArrayList<>();

        for (Store s : ownerStores) {
            StoreResponseDto storeResponseDto = StoreResponseDto.builder()
                    .storeId(s.getId())
                    .address(s.getAddress())
                    .phoneNumber(s.getPhoneNumber())
                    .storeName(s.getStoreName())
                    .build();

            responseStores.add(storeResponseDto);
        }

        return responseStores;
    }

}
