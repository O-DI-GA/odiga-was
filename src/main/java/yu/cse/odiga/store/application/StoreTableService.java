package yu.cse.odiga.store.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.dto.CreateStoreTableResponseDto;
import yu.cse.odiga.store.dto.StoreTableRegisterDto;
import yu.cse.odiga.store.dto.StoreTableResponseDto;
import yu.cse.odiga.store.type.TableStatus;

@Service
@RequiredArgsConstructor
public class StoreTableService {
	private final StoreTableRepository storeTableRepository;
	private final StoreRepository storeRepository;

	@Transactional
	public CreateStoreTableResponseDto createStoreTable(Long storeId, StoreTableRegisterDto storeTableRegisterDto) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		List<StoreTable> storeTables = store.getTables();

		for (StoreTable storeTable : storeTables) {
			if (storeTable.getTableNumber() == storeTableRegisterDto.getTableNumber()) {
				throw new BusinessLogicException("이미 존재 하는 테이블 번호 입니다.", HttpStatus.BAD_REQUEST.value());
			}
		}

		if (storeTableRegisterDto.getTableNumber() > store.getTableCount()) {
			throw new BusinessLogicException("최대 테이블 수 를 초과하였습니다.", HttpStatus.BAD_REQUEST.value());
		}

		StoreTable storeTable = StoreTable.builder()
			.tableNumber(storeTableRegisterDto.getTableNumber())
			.store(store)
			.maxSeatCount(storeTableRegisterDto.getMaxSeatCount())
			.tableStatus(TableStatus.EMPTY)
			.isPlaced(false)
			.build();

		return new CreateStoreTableResponseDto(storeTableRepository.save(storeTable).getId());
	}

	@Transactional
	public void updateStoreTable(Long storeId, Long storeTableId, StoreTableRegisterDto storeTableRegisterDto) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		if (storeTableRegisterDto.getTableNumber() > store.getTableCount()) {
			throw new BusinessLogicException("최대 테이블 수 를 초과하였습니다.", HttpStatus.BAD_REQUEST.value());
		}

		StoreTable table = storeTableRepository.findById(storeTableId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		// List<StoreTable> storeTables = store.getTables();
		//
		// for (StoreTable storeTable : storeTables) {
		// 	if (storeTable.getTableNumber() == tableRegisterDto.getTableNumber()) {
		// 		throw new BusinessLogicException("이미 존재 하는 테이블 번호 입니다.", HttpStatus.BAD_REQUEST.value());
		// 	}
		// }

		table.updateStoreTable(storeTableRegisterDto);
	}

	@Transactional
	public void deleteStoreTableByStoreTableId(Long storeId, Long storeTableId) {
		storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		StoreTable table = storeTableRepository.findById(storeTableId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		storeTableRepository.delete(table);
	}

	public List<StoreTableResponseDto> findAllStoreTablesByStoreId(Long storeId) {
		List<StoreTable> storeTables = storeTableRepository.findByStoreId(storeId);

		return storeTables.stream()
			.map(StoreTableResponseDto::from)
			.toList();
	}

	@Transactional
	public void togglePlaced(Long storeId, int tableNumber) {
		storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		StoreTable table = storeTableRepository.findByStoreIdAndTableNumber(storeId, tableNumber)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		table.togglePlaced();
	}

	@Transactional
	public boolean isPlaceable(Long storeId, int tableNumber) {
		storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		StoreTable table = storeTableRepository.findByStoreIdAndTableNumber(storeId, tableNumber)
			.orElseThrow(() -> new BusinessLogicException("올바른 접근이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		return !table.isPlaced();
	}
}
