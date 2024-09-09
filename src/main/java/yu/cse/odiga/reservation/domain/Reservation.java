package yu.cse.odiga.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.store.domain.Store;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;

    @Column
    private LocalDateTime reservationDateTime;

    @Column
    private int peopleCount;

}
