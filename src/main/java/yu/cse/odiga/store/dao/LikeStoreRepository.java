package yu.cse.odiga.store.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.store.domain.LikeStore;
import yu.cse.odiga.store.domain.Store;

@Repository
public interface LikeStoreRepository extends JpaRepository<LikeStore, Long> {
	Optional<LikeStore> findByUserAndStore(User user, Store store);

	List<LikeStore> findByUserId(Long userId);
}
