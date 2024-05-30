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
public class StoreImage{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String postImageUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "storeId")
    private Store store;

    private LocalDateTime createDate;

    @PrePersist
    public void createDate(){
        this.createDate = LocalDateTime.now();
    }
}