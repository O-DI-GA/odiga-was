package yu.cse.odiga.store.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.*;
import org.locationtech.jts.geom.Point;
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
    private Long id;

    @Column
    private String storeName;

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Column
    private int tableCount;

    @Builder.Default
    private int reviewCount = 0;

    @Builder.Default
    private int likeCount = 0;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Owner owner;

    @Column
    private String storeTitleImage;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImage> storeImages = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeStore> likeStores = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreTable> tables = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Waiting> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Review> reviewList = new ArrayList<>();
}
