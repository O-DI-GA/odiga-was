package yu.cse.odiga.waiting.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitingRegisterDto {
    private int peopleCount;
    private List<WaitingMenuDto> registerMenus;
}
