package yu.cse.odiga.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    
    // TODO : waiting 번호를 따로 필드로 만들어서 사용해야할듯

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Waiting> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Review> reviewList = new ArrayList<>();
}
