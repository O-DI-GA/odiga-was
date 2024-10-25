package yu.cse.odiga.store.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TableOrderRegisterDto {
    private List<TableOrderMenuforRegister> tableOrderMenuforRegisters;
}
