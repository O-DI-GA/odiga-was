package yu.cse.odiga.store.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import yu.cse.odiga.owner.domain.Owner;

@Entity
@Getter
@Setter
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
    private int reviewCount = 0;

    @ManyToOne
    @JoinColumn
    private Owner owner;
}