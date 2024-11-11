package yu.cse.odiga.store.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.application.OwnerStoreService;
import yu.cse.odiga.store.dto.StoreRegisterDto;
import yu.cse.odiga.store.dto.StoreResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/owner/store")
public class OwnerStoreController {
	private final OwnerStoreService ownerStoreService;

	@PostMapping()
	public ResponseEntity<?> registerStore(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
		@ModelAttribute  // modelattribute로 수정
		StoreRegisterDto storeRegisterDto) {
		ownerStoreService.registerStore(ownerUserDetails, storeRegisterDto);
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "created store", null));
	}

	@GetMapping()
	public ResponseEntity<?> findOwnerStores(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
		List<StoreResponseDto> stores = ownerStoreService.findOwnerStore(ownerUserDetails);

		return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find stores", stores));
	}

	@GetMapping("{storeId}")
	public ResponseEntity<?> findOwnerStoreDetail(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
		@PathVariable Long storeId) {
		return ResponseEntity.status(200)
			.body(new DefaultResponse<>(201, "store details",
				ownerStoreService.findOwnerStoreDetail(ownerUserDetails, storeId)));

	}

	@PutMapping("{storeId}")
	public ResponseEntity<?> updateStore(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
		@PathVariable Long storeId,
		@ModelAttribute StoreRegisterDto storeRegisterDto) {
		ownerStoreService.updateStore(ownerUserDetails, storeId, storeRegisterDto);
		return ResponseEntity.status(201).body(new DefaultResponse<>(201, "updated store", null));

	}
}