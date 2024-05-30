package yu.cse.odiga.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreTItleImage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String postImageUrl;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "storeId")
    private Store store;

    private LocalDateTime createDate;
}
