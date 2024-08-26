package yu.cse.odiga.menu.controller;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.menu.application.OwnerMenuService;
import yu.cse.odiga.menu.dto.CategoryDto;
import yu.cse.odiga.menu.dto.MenuRegisterDto;
import yu.cse.odiga.menu.dto.MenuResponseDto;
import yu.cse.odiga.owner.domain.OwnerUserDetails;

@RestController
@RequestMapping("api/v1/owner")
@RequiredArgsConstructor
public class OwnerMenuController {

    private final OwnerMenuService ownerMenuService;

    @PostMapping("/{storeId}/category")
    public ResponseEntity<?> registerCategory(@PathVariable Long storeId,
                                              @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                              @RequestBody CategoryDto categoryDto) {
        ownerMenuService.categoryRegister(ownerUserDetails, storeId, categoryDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "created category", null));
    }

    @GetMapping("/{storeId}/category")
    public ResponseEntity<?> findCategory(@PathVariable Long storeId,
                                          @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        List<CategoryDto> categories = ownerMenuService.findAllCategoryByStoreId(ownerUserDetails, storeId);

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find categories", categories));
    }

    @PostMapping("/{storeId}/category/{categoryId}/menu")
    public ResponseEntity<?> registerMenu(@PathVariable Long storeId, @PathVariable Long categoryId,
                                          @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                          @ModelAttribute  // modelattribute로 수정
                                          MenuRegisterDto menuRegisterDto) throws IOException {
        ownerMenuService.menuRegister(ownerUserDetails, storeId, categoryId, menuRegisterDto);
        return ResponseEntity.status(201).body(new DefaultResponse<>(201, "created menu", null));
    }

    @GetMapping("/{storeId}/category/{categoryId}/menu")
    public ResponseEntity<?> findStoreMenu(@PathVariable Long storeId, @PathVariable Long categoryId,
                                           @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        List<MenuResponseDto> menus = ownerMenuService.findMenu(ownerUserDetails, storeId, categoryId);

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find menus", menus));
    }
}
