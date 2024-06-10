package yu.cse.odiga.store.dao;

import java.util.Optional;
import org.geolatte.geom.M;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.store.domain.Menu;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
