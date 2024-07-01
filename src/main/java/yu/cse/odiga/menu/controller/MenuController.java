package yu.cse.odiga.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.menu.application.MenuService;

@RestController
@RequestMapping("api/v1/store/{storeId}/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("{storeId}")
    ResponseEntity<?> storeDetailMenus(@PathVariable Long storeId) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " Store Menus", menuService.findStoreMenus(storeId)));
    }
}
