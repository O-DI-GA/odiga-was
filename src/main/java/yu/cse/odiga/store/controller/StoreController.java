package yu.cse.odiga.store.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.StoreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/store/")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("{storeId}")
    ResponseEntity<?> storeDetails(@PathVariable Long storeId) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " store details", storeService.findByStoreId(storeId)));
    }

    @GetMapping("{storeId}/storeimages")
    ResponseEntity<?> storeImages(@PathVariable Long storeId) {
        return ResponseEntity.status(200)
                .body(new DefaultResponse<>(200, storeId + " store images", storeService.findStoreImagesByStoreId(storeId)));
    }

}
