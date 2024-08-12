package yu.cse.odiga.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.store.type.Rating;

@Builder
@Getter
public class ReviewRegisterDto {
    private String content;
    private Rating rating;
    private MultipartFile image;
}