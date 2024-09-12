package yu.cse.odiga.menu.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class MenuRegisterDto {
    private String menuName;
    private int price;
    private String caption;
    private MultipartFile menuImage;

    // Menu 수정용 필드
    private Long categoryId;
}
