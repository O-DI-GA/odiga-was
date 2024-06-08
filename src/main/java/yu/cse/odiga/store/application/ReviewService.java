package yu.cse.odiga.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.store.dao.ReviewRepository;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Review;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.dto.ReviewRegisterDto;
import yu.cse.odiga.store.dto.ReviewResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final S3ReviewImageUploadService s3ReviewImageUploadService;

    public void registerReview(Long storeId, ReviewRegisterDto reviewRegisterDto, CustomUserDetails customUserDetails) throws IOException {
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
        updateReviewCount(store);
    }

    private void updateReviewCount(Store store) {
        int currentReviewCount = store.getReviewCount();
        store.setReviewCount(currentReviewCount + 1);
        storeRepository.save(store);
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
}