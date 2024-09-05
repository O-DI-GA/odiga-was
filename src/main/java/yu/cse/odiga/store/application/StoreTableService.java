package yu.cse.odiga.store.application;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.dto.CreateStoreTableResponseDto;
import yu.cse.odiga.store.dto.StoreTableResponseDto;
import yu.cse.odiga.store.dto.TableRegisterDto;
import yu.cse.odiga.store.type.TableStatus;

@Service
@RequiredArgsConstructor
public class StoreTableService {
	private final StoreTableRepository storeTableRepository;
	private final StoreRepository storeRepository;

	@Transactional
	public CreateStoreTableResponseDto createStoreTable(Long storeId, TableRegisterDto tableRegisterDto) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		if (tableRegisterDto.getTableNumber() >= store.getTableCount()) {
			throw new BusinessLogicException("최대 테이블 수 를 초과하였습니다.", HttpStatus.BAD_REQUEST.value());
		}

		StoreTable storeTable = StoreTable.builder()
			.tableNumber(tableRegisterDto.getTableNumber())
			.store(store)
			.maxSeatCount(tableRegisterDto.getMaxSeatCount())
			.tableStatus(TableStatus.EMPTY)
			.build();

		return new CreateStoreTableResponseDto(storeTableRepository.save(storeTable).getId());
	}

	@Transactional
	public void updateStoreTable(Long storeId, Long storeTableId, TableRegisterDto tableRegisterDto) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		if (tableRegisterDto.getTableNumber() >= store.getTableCount()) {
			throw new BusinessLogicException("최대 테이블 수 를 초과하였습니다.", HttpStatus.BAD_REQUEST.value());
		}

		StoreTable storeTable = storeTableRepository.findById(storeTableId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		storeTable.updateStoreTable(tableRegisterDto);
	}

	public List<StoreTableResponseDto> findAllStoreTablesByStoreId(Long storeId) {
		List<StoreTable> storeTables = storeTableRepository.findByStoreId(storeId);

		return storeTables.stream()
			.map(StoreTableResponseDto::from)
			.toList();
	}

}
