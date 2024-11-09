package yu.cse.odiga.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.TableOrderService;
import yu.cse.odiga.store.dto.CallStaffRequestDto;
import yu.cse.odiga.store.dto.TableOrderManageDto;

@RestController
@RequestMapping("api/v1/table/{storeId}/order")
@RequiredArgsConstructor
public class TableOrderController {
	private final TableOrderService tableOrderService;

	@PostMapping("{storeTableNumber}")
	public ResponseEntity<?> registerTableOrderList(@PathVariable Long storeId, @PathVariable int storeTableNumber,
		@RequestBody TableOrderManageDto tableOrderManageDto) throws FirebaseMessagingException {
		tableOrderService.registerTableOrderList(storeId, storeTableNumber, tableOrderManageDto);
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Register Table order history", null));
	}

	@GetMapping("{storeTableNumber}")
	public ResponseEntity<?> getTableOrderList(@PathVariable Long storeId, @PathVariable int storeTableNumber) {
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "StoreTable order history",
			tableOrderService.getInSueTableOrderListByStoreIdAndTableNumber(storeId, storeTableNumber)));
	}

	@DeleteMapping("{storeTableNumber}")
	public ResponseEntity<?> deleteTableOrderList(@PathVariable Long storeId, @PathVariable int storeTableNumber,
		@RequestBody TableOrderManageDto tableOrderManageDto) throws FirebaseMessagingException {
		tableOrderService.cancelTableOrderList(storeId, storeTableNumber, tableOrderManageDto);
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "Delete Table order history", null));
	}

	@GetMapping
	public ResponseEntity<?> getAllTableOrderList(@PathVariable Long storeId) {
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "StoreTable order history",
			tableOrderService.getAllInuseTableOrderList(storeId)));
	}

	@PostMapping("call")
	public ResponseEntity<?> callStaff(@PathVariable Long storeId,
		@RequestBody CallStaffRequestDto callStaffRequestDto) throws FirebaseMessagingException {

		tableOrderService.callStaff(storeId, callStaffRequestDto);
		return ResponseEntity.ok().body(new DefaultResponse<>(200, "직원 호출을 성공 했습니다.", null));
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
