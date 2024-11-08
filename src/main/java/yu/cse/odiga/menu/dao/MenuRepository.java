package yu.cse.odiga.menu.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yu.cse.odiga.menu.domain.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	Optional<Menu> findByCategoryIdAndId(Long categoryId, Long menuId);

	Optional<Menu> findByCategory_Store_IdAndMenuName(Long storeId, String menuName);
}
