package yu.cse.odiga.store.controller;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.owner.domain.OwnerUserDetails;
import yu.cse.odiga.store.application.OwnerStoreService;
import yu.cse.odiga.store.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/owner/store")
public class OwnerStoreController {
    private final OwnerStoreService ownerStoreService;

    @PostMapping("register")
    public ResponseEntity<?> registerStore(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                           @ModelAttribute  // modelattribute로 수정
                                           StoreRegisterDto storeRegisterDto) {
        ownerStoreService.storeRegister(ownerUserDetails, storeRegisterDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "created store", null));
    }

    @GetMapping()
    public ResponseEntity<?> findOwnerStores(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        List<StoreResponseDto> stores = ownerStoreService.findOwnerStore(ownerUserDetails);

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find stores", stores));
    }

    @PostMapping("/{storeId}/category")
    public ResponseEntity<?> registerCategory(@PathVariable Long storeId,
                                              @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                              @RequestBody CategoryDto categoryDto) throws IOException {
        ownerStoreService.categoryRegister(ownerUserDetails, storeId, categoryDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "created category", null));
    }

    @GetMapping("/{storeId}/category")
    public ResponseEntity<?> findCategory(@PathVariable Long storeId,
                                          @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        List<CategoryDto> categories = ownerStoreService.findCategory(ownerUserDetails, storeId);

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find categories", categories));
    }

    @PostMapping("/{storeId}/category/{categoryId}/menu")
    public ResponseEntity<?> registerMenu(@PathVariable Long storeId, @PathVariable Long categoryId,
                                          @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                          @ModelAttribute  // modelattribute로 수정
                                          MenuRegisterDto menuRegisterDto) throws IOException {
        ownerStoreService.menuRegister(ownerUserDetails, storeId, categoryId, menuRegisterDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "created menu", null));
    }

    @GetMapping("/{storeId}/category/{categoryId}/menu")
    public ResponseEntity<?> findStoreMenu(@PathVariable Long storeId, @PathVariable Long categoryId,
                                           @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        List<MenuResponseDto> menus = ownerStoreService.findMenu(ownerUserDetails, storeId, categoryId);

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find menus", menus));
    }
}
