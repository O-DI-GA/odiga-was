package yu.cse.odiga.waiting.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String waitingCode;

    private int waitingNumber;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    @ManyToOne
    @JoinColumn
    private Store store;

    @ManyToOne
    @JoinColumn
    private User user;

    public void changeWaitingStatusToComplete() {
        this.waitingStatus = WaitingStatus.COMPLETE;
    }

    public boolean isIncomplete() {
        return this.waitingStatus == WaitingStatus.INCOMPLETE;
     }
}
