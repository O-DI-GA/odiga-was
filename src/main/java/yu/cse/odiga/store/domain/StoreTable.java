package yu.cse.odiga.store.domain;


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
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import yu.cse.odiga.store.type.TableStatus;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber;

    private int maxSeatCount;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;

    @Enumerated(EnumType.STRING)
    private TableStatus tableStatus;

    @OneToMany(mappedBy = "storeTable", fetch = FetchType.LAZY)
    private List<TableOrder> tableOrderList = new ArrayList<>();
}
