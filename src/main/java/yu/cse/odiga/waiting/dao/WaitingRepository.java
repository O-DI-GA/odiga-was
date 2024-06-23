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

    List<Waiting> findByUserIdAndWaitingStatus(Long userId, WaitingStatus waitingStatus);

    List<Waiting> findByStoreIdAndWaitingStatus(Long userId, WaitingStatus waitingStatus);

    Optional<Waiting> findByStoreIdAndUserId(Long storeId, Long userId);

    Optional<Waiting> findByStoreIdAndUserIdAndWaitingStatus(Long storeId, Long userId, WaitingStatus waitingStatus);

    Optional<Waiting> findByWaitingCodeAndStoreId(String waitingCode, Long storeId);

    List<Waiting> findByWaitingCodeAndStoreIdAndWaitingStatus(String waitingCode, Long storeId,
                                                              WaitingStatus waitingStatus);

    Optional<Waiting> findByStoreIdAndWaitingNumber(Long storeId, int waitingNumber);


}
