package yu.cse.odiga.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.store.application.LikeStoreService;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.util.DefaultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/like")
public class LikeStoreController {

    private final LikeStoreService likeStoreService;

    @PostMapping("/{storeId}")
    public ResponseEntity<?> add(@PathVariable Long storeId, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201,"like success", likeStoreService.add(storeId, userDetails)));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> delete(@PathVariable Long storeId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201,"dislike success", likeStoreService.delete(storeId, userDetails)));
    }
}
