package yu.cse.odiga.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.auth.dao.ImageRepository;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.Image;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.auth.dto.UserProfileDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${spring.servlet.file.path}")
    private String uploadFolder;

    public void upload(MultipartFile file, User user) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadFolder + imageFileName);
        try {
            Files.write(imageFilePath, file.getBytes());
            Image image = Image.builder()
                    .postImageUrl(uploadFolder + imageFileName)
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .build();
            imageRepository.save(image);
            user.setProfileImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String profileImageUrl = (user.getProfileImage() != null) ? user.getProfileImage().getPostImageUrl() : null;

        return UserProfileDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
    }
}