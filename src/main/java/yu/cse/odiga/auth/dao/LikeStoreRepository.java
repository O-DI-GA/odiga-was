package yu.cse.odiga.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.auth.domain.LikeStore;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.store.domain.Store;

import java.util.Optional;

@Repository
public interface LikeStoreRepository extends JpaRepository<LikeStore, Long> {
    Optional<LikeStore> findByUserAndStore(User user, Store store);
}
