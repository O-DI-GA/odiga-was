package yu.cse.odiga.owner.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OwnerLoginDto {
    private String email;
    private String password;
}
