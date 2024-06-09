package yu.cse.odiga.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.store.domain.StoreImage;

import java.util.List;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
    List<StoreImage> findByStoreId(Long storeId);
}
