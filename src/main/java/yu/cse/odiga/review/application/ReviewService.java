package yu.cse.odiga.review.application;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.review.dao.ReviewRepository;
import yu.cse.odiga.store.application.S3ReviewImageUploadService;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.review.domain.Review;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.review.dto.ReviewRegisterDto;
import yu.cse.odiga.review.dto.ReviewResponseDto;

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
}