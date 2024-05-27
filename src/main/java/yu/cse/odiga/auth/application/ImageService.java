package yu.cse.odiga.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yu.cse.odiga.auth.dao.ImageRepository;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.auth.dto.UserProfileDto;

@Service
@RequiredArgsConstructor
public class ImageService {


    private final UserRepository userRepository;
//private final ImageRepository imageRepository;
//import org.springframework.web.multipart.MultipartFile;
//import yu.cse.odiga.auth.domain.ProfileImage;
//import java.time.LocalDateTime;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Value;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//    @Value("${spring.servlet.file.path}")
//    private String uploadFolder;
//    public void upload(MultipartFile file, User user) {
//        UUID uuid = UUID.randomUUID();
//        String imageFileName = uuid + "_" + file.getOriginalFilename();
//        Path imageFilePath = Paths.get(uploadFolder + imageFileName);
//        try {
//            Files.write(imageFilePath, file.getBytes());
//            // Image 엔티티 저장
//            ProfileImage profileImage = ProfileImage.builder()
//                    .postImageUrl(uploadFolder + imageFileName)
//                    .user(user)
//                    .createDate(LocalDateTime.now())
//                    .build();
//            imageRepository.save(profileImage);
//            user.setProfileImage(profileImage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String profileImageUrl = (user.getProfileImage() != null) ? user.getProfileImage().getPostImageUrl(): null;

        return UserProfileDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
