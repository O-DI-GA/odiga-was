package yu.cse.odiga.store.dao;

import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.store.domain.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByOwnerId(Long ownerId);

    Optional<Store> findByOwnerIdAndId(Long ownerId, Long storeId);

    @Query(value = "select s from Store s where st_dwithin(s.location, :point, :distance) = true")
    List<Store> findAroundStores(@Param("point") Point point, @Param("distance") double distance);

    @Query(value = "select s from Store s where st_dwithin(s.location, :point, :distance) = true order by s.likeCount DESC")
    List<Store> findStoresRangeAndOrderByLikeCount(@Param("point") Point point, @Param("distance") double distance);

    @Query(value = "select s from Store s where st_dwithin(s.location, :point, :distance) = true order by s.reviewCount DESC")
    List<Store> findStoresRangeAndOrderByReviewCount(@Param("point") Point point, @Param("distance") double distance);

    @Query(value = "SELECT s FROM Store s JOIN FETCH s.waitingList w WHERE st_dwithin(s.location, :point, :distance) = true AND w.waitingStatus = 'INCOMPLETE' ORDER BY SIZE(w) DESC")
    List<Store> findStoresRangeAndOrderByWaitingCount(@Param("point") Point point, @Param("distance") double distance);


}
