package yu.cse.odiga.waiting.dao;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yu.cse.odiga.waiting.domain.Waiting;
import yu.cse.odiga.waiting.type.WaitingStatus;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findByUserId(Long userId);

    List<Waiting> findByStoreId(Long storeId);

    Optional<Waiting> findByStoreIdAndUserId(Long storeId, Long userId);

    Optional<Waiting> findByWaitingCodeAndStoreId(String waitingCode, Long storeId);
}
