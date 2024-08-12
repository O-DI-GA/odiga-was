package yu.cse.odiga.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.global.util.DefaultResponse;
import yu.cse.odiga.store.application.LikeStoreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/like")
public class LikeStoreController {

    private final LikeStoreService likeStoreService;

    @PostMapping("/{storeId}")
    public ResponseEntity<?> add(@PathVariable Long storeId, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201,"like success", likeStoreService.add(storeId, customUserDetails)));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> delete(@PathVariable Long storeId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201,"dislike success", likeStoreService.delete(storeId, customUserDetails)));
    }

    @GetMapping("/list")
    public ResponseEntity<?> findUserLikedStores(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(new DefaultResponse<>(200, "find user liked stores", likeStoreService.findUserLikedStores(customUserDetails)));
    }
}