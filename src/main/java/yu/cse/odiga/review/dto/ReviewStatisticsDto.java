package yu.cse.odiga.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewStatisticsDto {
    private Double averageRating;       // 평균 평점
    private Map<Integer, Long> ratingCounts; // 평점별 개수
}
