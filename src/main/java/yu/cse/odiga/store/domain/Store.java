package yu.cse.odiga.store.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import yu.cse.odiga.owner.domain.Owner;

import java.util.List;

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

    @Column
    private String storeName;

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @Column
    private int tableCount;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Owner owner;

    @OneToOne(cascade = CascadeType.ALL)
    private StoreTItleImage storeTItleImage;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImage> storeImages;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeStore> likeStores;

    @Column
    private Integer likeCount = 0;
}
