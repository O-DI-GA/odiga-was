package yu.cse.odiga.history.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yu.cse.odiga.global.type.BaseEntity;
import yu.cse.odiga.store.domain.Store;

import java.time.DayOfWeek;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class VisitCount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Store store;

    @Column(name = "visit_hour")
    private int visitHour;

    private int count = 0;

    private DayOfWeek dayOfWeek;

    public void incrementCount() {
        this.count++;
    }
}