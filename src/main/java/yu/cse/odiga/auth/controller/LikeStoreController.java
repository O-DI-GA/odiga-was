package yu.cse.odiga.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yu.cse.odiga.auth.application.LikeStoreService;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.LikeStore;
import yu.cse.odiga.auth.dto.LikeStoreDto;
import yu.cse.odiga.global.util.DefaultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/likestore")
public class LikeStoreController {

    private final LikeStoreService likeStoreService;

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestBody LikeStoreDto likeStoreDto, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201,"like success", likeStoreService.like(likeStoreDto)));
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<?> dislike(@RequestBody LikeStoreDto likeStoreDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResponse<>(201,"like success", likeStoreService.dislike(likeStoreDto)));
    }
}
