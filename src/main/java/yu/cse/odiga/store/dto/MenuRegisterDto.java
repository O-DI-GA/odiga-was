package yu.cse.odiga.store.dto;

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
    private String category;
}
