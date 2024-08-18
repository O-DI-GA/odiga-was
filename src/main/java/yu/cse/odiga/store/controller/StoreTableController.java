package yu.cse.odiga.store.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.StoreTableService;
import yu.cse.odiga.store.dto.TableRegisterDto;

@RestController
@RequestMapping("api/v1/owner/store/{storeId}/tables")
@RequiredArgsConstructor
public class StoreTableController {

    private final StoreTableService storeTableService;


    @PostMapping
    public ResponseEntity<?> creatStoreTable(@PathVariable Long storeId,
                                             @RequestBody TableRegisterDto tableRegisterDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201, "create store table", storeTableService.createStoreTable(storeId, tableRegisterDto)));
    }
}
