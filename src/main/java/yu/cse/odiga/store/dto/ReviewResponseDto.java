package yu.cse.odiga.store.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String storeName;
    private String content;
    private int rating;
    private String imageUrl;
    private String userNickname;
    private String userProfileImageUrl;
    private LocalDateTime createDate;
}