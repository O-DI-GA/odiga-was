package yu.cse.odiga.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import yu.cse.odiga.store.domain.Store;

import java.time.LocalDateTime;

@Builder
@Getter
public class AvailableReservationTimeResponseDto {

    private Long storeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime availableReservationTime;

    private boolean isAvailable;

}
