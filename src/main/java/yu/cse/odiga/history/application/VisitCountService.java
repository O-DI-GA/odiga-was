package yu.cse.odiga.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.history.dao.VisitCountRepository;
import yu.cse.odiga.history.domain.VisitCount;
import yu.cse.odiga.history.dto.DayVisitCountDto;
import yu.cse.odiga.history.dto.HourlyVisitCountDto;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.domain.Store;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitCountService {
    private final VisitCountRepository visitCountRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void incrementVisitCount(Long storeId, int hour) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException("존재하지 않는 Store Id 입니다.", HttpStatus.BAD_REQUEST.value()));

        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();

        VisitCount visitCount = visitCountRepository.findByStoreAndVisitHourAndDayOfWeekAndCreatedAtBetween(
                        store, hour, dayOfWeek, LocalDateTime.now().withHour(0).withMinute(0),
                        LocalDateTime.now().withHour(23).withMinute(59))
                .orElseGet(() -> {
                    VisitCount newVisitCount = new VisitCount();
                    newVisitCount.setStore(store);
                    newVisitCount.setVisitHour(hour);
                    newVisitCount.setDayOfWeek(dayOfWeek);
                    return visitCountRepository.save(newVisitCount);
                });

        visitCount.incrementCount();
        visitCountRepository.save(visitCount);
    }

    public Map<String, List<HourlyVisitCountDto>> getMonthlyHourlyVisitCounts(Long storeId) {
        List<VisitCount> visitCounts = visitCountRepository.findByStore_Id(storeId);

        return visitCounts.stream().collect(Collectors.groupingBy(
                vc -> YearMonth.from(vc.getCreatedAt()).toString(),
                Collectors.mapping(
                        vc -> new HourlyVisitCountDto(vc.getVisitHour() + ":00", vc.getCount()),
                        Collectors.toList()
                )
        ));
    }

    public Map<String, List<DayVisitCountDto>> getMonthlyDayVisitCounts(Long storeId) {
        List<VisitCount> visitCounts = visitCountRepository.findByStore_Id(storeId);

        return visitCounts.stream().collect(Collectors.groupingBy(
                vc -> YearMonth.from(vc.getCreatedAt()).toString(),
                Collectors.mapping(
                        vc -> new DayVisitCountDto(vc.getDayOfWeek().name(), vc.getCount()),
                        Collectors.toList()
                )
        ));
    }
}