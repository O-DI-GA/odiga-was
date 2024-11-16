package yu.cse.odiga.history.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.global.type.BaseEntity;
import yu.cse.odiga.store.domain.Store;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UseHistory extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	private int paymentAmount;

	@OneToMany(mappedBy = "history")
	private List<HistoryMenu> historyMenus;

	@ManyToOne
	private Store store;
}
