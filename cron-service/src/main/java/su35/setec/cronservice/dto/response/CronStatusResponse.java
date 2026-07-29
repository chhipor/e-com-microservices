package su35.setec.cronservice.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CronStatusResponse {

    private Instant lastMinuteRun;
    private Instant lastMidnightRun;
    private Instant lastSundayRun;
    private UUID lastMinuteReportId;
    private UUID lastMidnightReportId;
    private UUID lastSundayReportId;
}
