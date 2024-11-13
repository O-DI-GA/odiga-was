package yu.cse.odiga.review.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.review.domain.Review;
import yu.cse.odiga.review.dto.RatingCountDto;
import yu.cse.odiga.store.type.Rating;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreId(Long storeId);
    List<Review> findByUserId(Long userId);

    @Query("SELECT r.rating FROM Review r WHERE r.store.id = :storeId")
    List<Rating> findAverageRatingByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT new yu.cse.odiga.review.dto.RatingCountDto(r.rating, COUNT(r)) " +
            "FROM Review r WHERE r.store.id = :storeId " +
            "GROUP BY r.rating")
    List<RatingCountDto> findRatingCountsByStoreId(@Param("storeId") Long storeId);

}