package yu.cse.odiga.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.domain.CustomUserDetails;
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
        String userNickname = customUserDetails.getUser().getNickname();
        String profileImageUrl = customUserDetails.getProfileImageUrl() != null ? customUserDetails.getProfileImageUrl() : null;
        String uploadImageUrl = s3ReviewImageUploadService.upload(reviewRegisterDto.getImage());

        Review review = Review.builder()
                .content(reviewRegisterDto.getContent())
                .rating(reviewRegisterDto.getRating())
                .imageUrl(uploadImageUrl)
                .store(store)
                .userNickname(userNickname)
                .createDate(LocalDateTime.now())
                .userProfileImageUrl(profileImageUrl)
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
            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .reviewId(review.getId())
                    .content(review.getContent())
                    .rating(review.getRating().getValue())
                    .imageUrl(review.getImageUrl())
                    .userNickname(review.getUserNickname())
                    .userProfileImageUrl(review.getUserProfileImageUrl())
                    .createDate(review.getCreateDate())
                    .build();

            responseReviews.add(reviewResponseDto);
        }

        return responseReviews;
    }
}