package yu.cse.odiga.waiting.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingCodeResponseDto {
    private String waitingCode;
}
