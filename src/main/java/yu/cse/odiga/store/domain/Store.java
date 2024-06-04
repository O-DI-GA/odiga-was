package yu.cse.odiga.store.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.*;
import yu.cse.odiga.owner.domain.Owner;
import yu.cse.odiga.waiting.domain.Waiting;
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
    private int reviewCount = 0;
    private int likeCount = 0;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Owner owner;

    @OneToOne(mappedBy = "store",cascade = CascadeType.ALL, orphanRemoval = true)
    private StoreTItleImage storeTItleImage;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImage> storeImages;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeStore> likeStores;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Waiting> waitingList = new ArrayList<>();

}
