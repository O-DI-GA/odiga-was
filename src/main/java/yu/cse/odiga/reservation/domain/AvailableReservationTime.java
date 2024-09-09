package yu.cse.odiga.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import yu.cse.odiga.store.domain.Store;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableReservationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Store store;

    private LocalDateTime availableReservationTime;

    private boolean isAvailable;


}
