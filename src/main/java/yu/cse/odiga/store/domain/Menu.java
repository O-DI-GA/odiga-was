package yu.cse.odiga.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String menuName;

    @Column
    private int price;

    @Column
    private String caption;

    @Column
    private String menuImageUrl;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Category category;
}
