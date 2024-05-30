package yu.cse.odiga.store.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.owner.domain.Owner;
import yu.cse.odiga.waiting.domain.Waiting;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String storeName;
    private String phoneNumber;
    private String address;
    private int tableCount;

    @ManyToOne
    @JoinColumn
    private Owner owner;


    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Waiting> waitingList = new ArrayList<>();

}
