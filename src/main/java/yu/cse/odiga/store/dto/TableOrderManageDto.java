package yu.cse.odiga.store.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TableOrderManageDto {
	private List<TableOrderMenuforManage> tableOrderMenuforManages;
}
