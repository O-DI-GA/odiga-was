package yu.cse.odiga.history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayVisitCountDto {
    private String day;
    private Integer visitCount;
}
