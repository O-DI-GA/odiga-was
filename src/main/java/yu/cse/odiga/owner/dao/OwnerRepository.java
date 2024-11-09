package yu.cse.odiga.owner.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yu.cse.odiga.owner.domain.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
	Optional<Owner> findByEmail(String email);
}
