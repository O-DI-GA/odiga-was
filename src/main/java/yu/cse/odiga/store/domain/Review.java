package yu.cse.odiga.store.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.auth.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    // TODO : User entity 랑 연관 맺어야함, score 0 ~ 5 점 enum 으로 바꾸는게 좋을듯 (정수)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String content;
    private double rating;
    private String imageUrl;
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn
    private Store store;
}