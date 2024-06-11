package yu.cse.odiga.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.StoreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("{storeId}")
    ResponseEntity<?> storeDetails(@PathVariable Long storeId) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " store details", storeService.findByStoreId(storeId)));
    }

    @GetMapping("{storeId}/menus")
    ResponseEntity<?> storeDetailMenus(@PathVariable Long storeId) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " Store Menus", storeService.findStoreMenus(storeId)));
    }

    @GetMapping("{storeId}/images")
    ResponseEntity<?> storeImages(@PathVariable Long storeId) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " store images",
                        storeService.findStoreImagesByStoreId(storeId)));
    }

    @GetMapping("map")
    ResponseEntity<?> findAroundStoreListInMap(@RequestParam("longitude") double longitude,
                                               @RequestParam("latitude") double latitude) {
        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find store",
                storeService.findAroundStoreInMap(latitude, longitude)));
    }

    @GetMapping()
    ResponseEntity<?> findAroundStoreList(@RequestParam("longitude") double longitude,
                                          @RequestParam("latitude") double latitude, @RequestParam("orderCondition")
                                          String orderCondition) {

        return ResponseEntity.status(200).body(new DefaultResponse<>(200, "find store",
                storeService.findAroundListStoreList(latitude, longitude, orderCondition)));
    }

}
