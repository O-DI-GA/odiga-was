package yu.cse.odiga.store.domain;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TableRegisterDto {
    private int tableNumber;
    private int maxSeatCount;
}
