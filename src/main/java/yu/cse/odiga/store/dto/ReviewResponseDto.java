package yu.cse.odiga.store.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String content;
    private double rating;
    private String imageUrl;
}