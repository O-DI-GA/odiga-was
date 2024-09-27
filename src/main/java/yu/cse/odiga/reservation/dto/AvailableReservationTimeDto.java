package yu.cse.odiga.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class AvailableReservationTimeDto {

    private Long storeId;
    private List<MonthSchedule> schedules;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MonthSchedule {
        private int year;
        private int month; // Month as an integer (1 for January, 2 for February, etc.)
        private List<DaySchedule> daySchedules;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DaySchedule {
        private String dayOfWeek; // Example: "MONDAY", "TUESDAY", etc.
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime endTime;
        private int intervalMinutes;
    }
}
