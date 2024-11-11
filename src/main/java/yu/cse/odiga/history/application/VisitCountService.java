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
import yu.cse.odiga.owner.domain.OwnerUserDetails;
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

    public Map<String, List<HourlyVisitCountDto>> getMonthlyHourlyVisitCounts(Long storeId, OwnerUserDetails ownerUserDetails) {
        List<VisitCount> visitCounts = visitCountRepository.findByStore_Id(storeId);

        return visitCounts.stream()
                .collect(Collectors.groupingBy(
                        vc -> YearMonth.from(vc.getCreatedAt()).toString(),
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        vc -> vc.getVisitHour() + ":00",
                                        Collectors.summingInt(VisitCount::getCount)
                                ),
                                hourlyMap -> hourlyMap.entrySet().stream()
                                        .map(entry -> new HourlyVisitCountDto(entry.getKey(), entry.getValue()))
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public Map<String, List<DayVisitCountDto>> getMonthlyDayVisitCounts(Long storeId, OwnerUserDetails ownerUserDetails) {
        List<VisitCount> visitCounts = visitCountRepository.findByStore_Id(storeId);

        return visitCounts.stream()
                .collect(Collectors.groupingBy(
                        vc -> YearMonth.from(vc.getCreatedAt()).toString(),
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        vc -> vc.getDayOfWeek().name(),
                                        Collectors.summingInt(VisitCount::getCount)
                                ),
                                dayMap -> dayMap.entrySet().stream()
                                        .map(entry -> new DayVisitCountDto(entry.getKey(), entry.getValue()))
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public List<HourlyVisitCountDto> getTodayHourlyVisitCounts(Long storeId, OwnerUserDetails ownerUserDetails) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<VisitCount> todayVisitCounts = visitCountRepository.findByStore_IdAndCreatedAtBetween(storeId, startOfDay, endOfDay);

        return todayVisitCounts.stream()
                .collect(Collectors.groupingBy(VisitCount::getVisitHour, Collectors.summingInt(VisitCount::getCount)))
                .entrySet().stream()
                .map(entry -> new HourlyVisitCountDto(entry.getKey() + ":00", entry.getValue()))
                .collect(Collectors.toList());
    }
}