package yu.cse.odiga.global.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
public class DefaultResponse<T> {

    private int httpStatusCode;
    private String responseMessage;
    private T data;
}