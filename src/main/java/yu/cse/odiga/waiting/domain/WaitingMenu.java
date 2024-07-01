package yu.cse.odiga.waiting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yu.cse.odiga.menu.domain.Menu;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Waiting waiting;
    @ManyToOne
    private Menu menu;

    private int menuCount;

    private int totalPrice;
}
