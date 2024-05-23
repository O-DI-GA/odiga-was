package yu.cse.odiga.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Image{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String caption;

    private String postImageUrl;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;

    private LocalDateTime createDate;

    @PrePersist
    public void createDate(){
        this.createDate = LocalDateTime.now();
    }
}

