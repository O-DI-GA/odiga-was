package yu.cse.odiga.store.dto;

import lombok.*;
import yu.cse.odiga.store.domain.TableOrderMenu;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TableOrderMenuforRegister {
    private String menuName;
    private int menuCount;
}
