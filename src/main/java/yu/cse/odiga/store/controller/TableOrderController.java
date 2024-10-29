package yu.cse.odiga.store.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.TableOrderService;
import yu.cse.odiga.store.dto.TableOrderMenuDto;
import yu.cse.odiga.store.dto.TableOrderRegisterDto;

@RestController
@RequestMapping("api/v1/table/{storeId}/order")
@RequiredArgsConstructor
public class TableOrderController {
	private final TableOrderService tableOrderService;

	@PostMapping("{storeTableNumber}")
	public ResponseEntity<?> registerTableOrderList(@PathVariable Long storeId, @PathVariable int storeTableNumber,
		@RequestBody TableOrderRegisterDto tableOrderRegisterDto) {
		tableOrderService.registerTableOrderList(storeId, storeTableNumber, tableOrderRegisterDto);
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Register Table order history", null));
	}

	@GetMapping("{storeTableNumber}")
	public ResponseEntity<?> getTableOrderList(@PathVariable Long storeId, @PathVariable int storeTableNumber) {
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "StoreTable order history",
			tableOrderService.getInSueTableOrderListByStoreIdAndTableNumber(storeId, storeTableNumber)));
	}

	@GetMapping
	public ResponseEntity<?> getAllTableOrderList(@PathVariable Long storeId) {
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "StoreTable order history",
			tableOrderService.getAllInuseTableOrderList(storeId)));
	}

	//TODO : 나중에 payments로 옮겨야함
	@GetMapping("{tableOrderId}/payment")
	public ResponseEntity<?> findTableOrderHistoryById(@PathVariable Long storeId,
		@PathVariable Long tableOrderId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new DefaultResponse<>(200, "Table Order history by id " + tableOrderId,
				tableOrderService.findByTableOrderHistoryByTableOrderId(tableOrderId)));
	}

}
