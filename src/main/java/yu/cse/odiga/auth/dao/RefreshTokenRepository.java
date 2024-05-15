package yu.cse.odiga.auth.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yu.cse.odiga.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserEmail(String email);

}
