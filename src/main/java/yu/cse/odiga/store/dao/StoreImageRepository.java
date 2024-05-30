package yu.cse.odiga.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.store.domain.StoreImage;

import java.util.List;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
}
