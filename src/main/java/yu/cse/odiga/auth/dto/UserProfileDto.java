package yu.cse.odiga.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDto {
    private String email;
    private String nickname;
    private String profileImageUrl; // 프로필 이미지 URL을 저장할 필드
}