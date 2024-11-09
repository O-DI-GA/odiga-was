package yu.cse.odiga.auth.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileUpdateDto {
	private String email;
	private String nickname;
	private MultipartFile profileImage;
}