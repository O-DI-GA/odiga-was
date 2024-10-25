package yu.cse.odiga.store.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import yu.cse.odiga.store.dto.StoreTableRegisterDto;
import yu.cse.odiga.store.type.TableStatus;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int tableNumber;

	private int maxSeatCount;

	private boolean isPlaced;

	@ManyToOne
	@JoinColumn(name = "storeId")
	private Store store;

	@Enumerated(EnumType.STRING)
	private TableStatus tableStatus;

	@OneToMany(mappedBy = "storeTable")
	private List<TableOrder> tableOrderList = new ArrayList<>();

	public boolean isTableEmpty() {
		return this.tableStatus == TableStatus.EMPTY;
	}

	public void addNewTableOrder(TableOrder tableOrder) {
		tableOrderList.add(tableOrder);
	}

	public void changeTableStatusToInUse() {
		this.tableStatus = TableStatus.INUSE;
	}

	public void changeTableStatusToEmpty() {
		this.tableStatus = TableStatus.EMPTY;
	}

	public void updateStoreTable(StoreTableRegisterDto storeTableRegisterDto) {
		this.tableNumber = storeTableRegisterDto.getTableNumber();
		this.maxSeatCount = storeTableRegisterDto.getMaxSeatCount();
	}

	public void togglePlaced() {
		this.isPlaced = !this.isPlaced;
	}
}
