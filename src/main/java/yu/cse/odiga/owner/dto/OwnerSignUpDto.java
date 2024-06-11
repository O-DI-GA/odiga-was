package yu.cse.odiga.owner.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OwnerSignUpDto {

    private String email;
    private String password;
    private String name;

}
