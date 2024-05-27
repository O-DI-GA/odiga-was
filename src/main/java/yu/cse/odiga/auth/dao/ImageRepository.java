package yu.cse.odiga.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.auth.domain.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
