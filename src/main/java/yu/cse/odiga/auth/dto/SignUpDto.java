package yu.cse.odiga.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
    private MultipartFile profileImage;
}