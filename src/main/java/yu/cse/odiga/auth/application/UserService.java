package yu.cse.odiga.auth.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.auth.dto.UserProfileDto;
import yu.cse.odiga.store.dao.ReviewRepository;
import yu.cse.odiga.store.domain.Review;
import yu.cse.odiga.store.dto.ReviewResponseDto;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final S3ProfileImageUploadService s3ProfileImageUploadService;

    public void updateUserProfile(String email, MultipartFile profileImage, String nickname) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정 입니다."));

        if(profileImage != null && !profileImage.isEmpty()) {
            try {
                String oldFileName = user.getProfileImageUrl();
                s3ProfileImageUploadService.updateFile(profileImage, oldFileName, user);
            } catch (IOException e) {
                throw new IllegalArgumentException("파일 업로드에 실패했습니다.");
            }
        }

        if (nickname != null && !nickname.isEmpty()) {
            user.setNickname(nickname);
        }

        userRepository.save(user);
    }
    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String profileImageUrl = (user.getProfileImageUrl() != null) ? user.getProfileImageUrl(): null;

        return UserProfileDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public List<ReviewResponseDto> findUserReviews(CustomUserDetails userDetails) {
        List<Review> reviews = reviewRepository.findByUserId(userDetails.getUser().getId());
        List<ReviewResponseDto> responseReviews = new ArrayList<>();

        for (Review review : reviews) {
            User user = review.getUser();
            String storeName = review.getStore().getStoreName();
            String userNickname = user.getNickname();
            String userProfileImageUrl = user.getProfileImageUrl();

            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .reviewId(review.getId())
                    .storeName(storeName)
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