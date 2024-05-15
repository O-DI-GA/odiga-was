package yu.cse.odiga.auth.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
}
