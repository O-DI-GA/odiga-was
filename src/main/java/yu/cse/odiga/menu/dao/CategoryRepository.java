package yu.cse.odiga.menu.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.menu.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    List<Category> findByStoreId(Long storeId);

    Optional<Category> findByStoreIdAndId(Long storeId, Long categoryId);
}
