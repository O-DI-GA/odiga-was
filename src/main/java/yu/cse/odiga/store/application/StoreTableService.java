package yu.cse.odiga.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.domain.TableRegisterDto;
import yu.cse.odiga.store.type.TableStatus;

@Service
@RequiredArgsConstructor
public class StoreTableService {
    private final StoreTableRepository storeTableRepository;
    private final StoreRepository storeRepository;

    public void createStoreTable(Long storeId, TableRegisterDto tableRegisterDto) {

        Store store = storeRepository.findById(storeId).orElseThrow();

        StoreTable storeTable = StoreTable.builder()
                .tableNumber(tableRegisterDto.getTableNumber())
                .store(store)
                .maxSeatCount(tableRegisterDto.getMaxSeatCount())
                .tableStatus(TableStatus.EMPTY)
                .build();

        storeTableRepository.save(storeTable);
    }
}
