package yu.cse.odiga.global.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int httpStatusCode;
    private String errorMessage;
}