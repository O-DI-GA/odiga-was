package yu.cse.odiga.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.store.domain.StoreTItleImage;

@Repository
public interface StoreTitleImageRepository extends JpaRepository<StoreTItleImage, Long> {
}
