package yu.cse.odiga.store.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Rating rating;
    private String imageUrl;
    private String userNickname;
    private String userProfileImageUrl;
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn
    private Store store;
}