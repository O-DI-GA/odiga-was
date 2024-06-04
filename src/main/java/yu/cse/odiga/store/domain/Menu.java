package yu.cse.odiga.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import yu.cse.odiga.store.type.Category;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String menuName;

    @Column
    private int price;

    @Column
    private String caption;

    @Column
    private String menuImageUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "storeId")
    private Store store;
}
