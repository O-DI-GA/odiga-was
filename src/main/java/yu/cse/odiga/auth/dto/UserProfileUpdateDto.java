package yu.cse.odiga.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UserProfileUpdateDto {
    private String email;
    private String nickname;
    private MultipartFile profileImage;
}