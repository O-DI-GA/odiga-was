package yu.cse.odiga.review.application;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.review.dao.ReviewRepository;
import yu.cse.odiga.review.dto.RatingCountDto;
import yu.cse.odiga.review.dto.ReviewStatisticsDto;
import yu.cse.odiga.store.application.S3ReviewImageUploadService;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.review.domain.Review;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.review.dto.ReviewRegisterDto;
import yu.cse.odiga.review.dto.ReviewResponseDto;
import yu.cse.odiga.store.type.Rating;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final S3ReviewImageUploadService s3ReviewImageUploadService;


    @Transactional
    public void registerReview(Long storeId, ReviewRegisterDto reviewRegisterDto, CustomUserDetails customUserDetails)
            throws IOException {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));

        String uploadImageUrl = s3ReviewImageUploadService.upload(reviewRegisterDto.getImage());

        User user = customUserDetails.getUser();

        Review review = Review.builder()
                .content(reviewRegisterDto.getContent())
                .rating(reviewRegisterDto.getRating())
                .imageUrl(uploadImageUrl)
                .store(store)
                .user(user)
                .createDate(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        store.increaseReviewCount();
    }

    public List<ReviewResponseDto> findStoreReviews(Long storeId) {
        List<Review> reviews = reviewRepository.findByStoreId(storeId);
        List<ReviewResponseDto> responseReviews = new ArrayList<>();

        for (Review review : reviews) {
            User user = review.getUser();
            String userNickname = user.getNickname();
            String userProfileImageUrl = user.getProfileImageUrl();

            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .reviewId(review.getId())
                    .content(review.getContent())
                    .rating(review.getRating().getValue())
                    .imageUrl(review.getImageUrl())
                    .userNickname(userNickname)
                    .userProfileImageUrl(userProfileImageUrl)
                    .createDate(review.getCreateDate())
                    .build();

            responseReviews.add(reviewResponseDto);
        }
        return responseReviews;
    }

    public ReviewStatisticsDto getReviewStatistics(Long storeId) {
        // 평균 평점
        List<Rating> averageRating = reviewRepository.findAverageRatingByStoreId(storeId);

        int sumRating = 0;
        for (Rating rating : averageRating) {
            sumRating += rating.getValue();
        }
        double avgRating = 0;
        if (averageRating.size() != 0) {
            avgRating = sumRating / averageRating.size();
        }

        // 평점별 개수
        List<RatingCountDto> rawCounts = reviewRepository.findRatingCountsByStoreId(storeId);

        HashMap<Integer, Long> counts = new HashMap<>();

        List<Rating> ratings = List.of(Rating.values());

        for (Rating rating : ratings) {
            counts.put(rating.getValue(), 0L);
        }

        for(RatingCountDto ratingCountDto : rawCounts) {
            counts.put(ratingCountDto.getRating().getValue(), ratingCountDto.getCount());
        }

        return ReviewStatisticsDto.builder()
                .averageRating(avgRating)
                .ratingCounts(counts)
                .build();
    }
}