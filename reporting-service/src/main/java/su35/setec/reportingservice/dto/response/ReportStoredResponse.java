package su35.setec.reportingservice.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportStoredResponse {

    private UUID id;
    private String reportType;
    private Instant generatedAt;
}
