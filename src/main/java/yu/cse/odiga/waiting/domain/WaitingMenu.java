package yu.cse.odiga.waiting.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.store.domain.Menu;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Waiting waiting;

    @OneToOne
    private Menu menus;

    private int menuCount;
}