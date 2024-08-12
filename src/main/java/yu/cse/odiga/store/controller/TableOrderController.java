package yu.cse.odiga.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.TableOrderService;

@RestController
@RequestMapping("api/v1/table/{storeId}/order")
@RequiredArgsConstructor
public class TableOrderController {
    private final TableOrderService tableOrderService;


    @GetMapping("{tableNumber}")
    public ResponseEntity<?> findTableOrderHistory(@PathVariable Long storeId, @PathVariable int tableNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(new DefaultResponse<>(200, "Table order history",
                tableOrderService.findTableOrderList(storeId, tableNumber)));
    }

    //TODO : 나중에 payments로 옮겨야함
    @GetMapping("{tableOrderHistoryId}/payment")
    public ResponseEntity<?> findTableOrderHistoryById(@PathVariable Long storeId,
                                                       @PathVariable Long tableOrderHistoryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DefaultResponse<>(200, "Table Order history by id " + tableOrderHistoryId,
                        tableOrderService.findByIdOrderHistory(tableOrderHistoryId)));
    }

}
