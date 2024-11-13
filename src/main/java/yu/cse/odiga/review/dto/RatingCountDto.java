package yu.cse.odiga.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import yu.cse.odiga.store.type.Rating;

@Data
@AllArgsConstructor
public class RatingCountDto {
    private Rating rating; // 또는 Enum Rating
    private Long count;
}
