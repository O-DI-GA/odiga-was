package yu.cse.odiga.menu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.menu.domain.Category;
import yu.cse.odiga.menu.domain.Menu;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByCategoryIdAndId(Long categoryId, Long menuId);

}
