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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yu.cse.odiga.store.type.PaymentStatus;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TableOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private StoreTable storeTable;

    @OneToMany(mappedBy = "tableOrder", fetch = FetchType.LAZY)
    private List<TableOrderMenu> tableOrderMenuList = new ArrayList<>();

    public int getTableTotalPrice() {
        return tableOrderMenuList.stream()
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getMenuCount())
                .sum();
    }

}
