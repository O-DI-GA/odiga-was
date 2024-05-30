package yu.cse.odiga.store.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.store.domain.Store;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByOwnerId(Long ownerId);
}
