package yu.cse.odiga.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.auth.domain.ProfileImage;

@Repository
public interface ImageRepository extends JpaRepository<ProfileImage, Integer> {
}
