package yu.cse.odiga.store.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class ReviewRegisterDto {
    private String content;
    private double rating;
    private MultipartFile image;
}