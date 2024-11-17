package yu.cse.odiga.store.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yu.cse.odiga.global.type.BaseEntity;
import yu.cse.odiga.store.type.PaymentStatus;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TableOrder extends BaseEntity {
	// 손님이있는 테이블의 주문내역
	// 주문하기 주문내역 , wating메뉴 리스트 참고
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	@ManyToOne
	@JoinColumn(name = "storeTableId")
	private StoreTable storeTable;

	@OneToMany(mappedBy = "tableOrder", fetch = FetchType.LAZY)
	private List<TableOrderMenu> tableOrderMenuList = new ArrayList<>();

	public void completeOrder() {
		storeTable.changeTableStatusToEmpty();
		this.paymentStatus = PaymentStatus.COMPLETE;
		// storeTable.getTableOrderList().clear();
	}

	public Store getStore() {
		return storeTable.getStore();
	}

	public int getTableTotalPrice() {
		return tableOrderMenuList.stream()
			.mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getMenuCount())
			.sum();
	}
}
