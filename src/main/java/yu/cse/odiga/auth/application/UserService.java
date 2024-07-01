package yu.cse.odiga.auth.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.auth.dto.UserProfileDto;
import yu.cse.odiga.auth.dto.UserProfileUpdateDto;
import yu.cse.odiga.store.dao.ReviewRepository;
import yu.cse.odiga.store.domain.Review;
import yu.cse.odiga.store.dto.ReviewResponseDto;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    @Value("${profile.default-image-url}")
    private String defaultProfileImageUrl;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final S3ProfileImageUploadService s3ProfileImageUploadService;

    public UserProfileDto updateUserProfile(CustomUserDetails customUserDetails, UserProfileUpdateDto userProfileUpdateDto) {

        System.out.println("[USER Profile Update] : " + userProfileUpdateDto.getNickname());
        System.out.println("[USER Profile Update] : " + userProfileUpdateDto.getNickname());
        System.out.println("[USER Profile Update] : " + userProfileUpdateDto.getProfileImage().getContentType());

        User user = userRepository.findByEmail(customUserDetails.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정 입니다."));

        if (userProfileUpdateDto.getProfileImage() != null && !userProfileUpdateDto.getProfileImage().isEmpty()) {
            try {
                if (user.getProfileImageUrl().equals(defaultProfileImageUrl)) {
                    s3ProfileImageUploadService.upload(userProfileUpdateDto.getProfileImage(), user);
                } else {
                    String oldFileName = user.getProfileImageUrl();
                    s3ProfileImageUploadService.updateFile(userProfileUpdateDto.getProfileImage(), oldFileName, user);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("파일 업로드에 실패했습니다.");
            }
        }

        if (userProfileUpdateDto.getNickname() != null && !userProfileUpdateDto.getNickname().isEmpty()) {
            user.setNickname(userProfileUpdateDto.getNickname());
        }

        userRepository.save(user);

        return UserProfileDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));
        String profileImageUrl = (user.getProfileImageUrl() != null) ? user.getProfileImageUrl() : null;

        return UserProfileDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public List<ReviewResponseDto> findUserReviews(CustomUserDetails customUserDetails) {
        List<Review> reviews = reviewRepository.findByUserId(customUserDetails.getUser().getId());
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