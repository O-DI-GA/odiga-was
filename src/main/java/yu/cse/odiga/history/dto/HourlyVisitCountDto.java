package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HourlyVisitCountDto {
    private String hour;
    private Integer visitCount;
}