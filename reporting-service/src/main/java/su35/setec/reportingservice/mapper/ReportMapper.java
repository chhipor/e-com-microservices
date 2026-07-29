package su35.setec.reportingservice.mapper;

import org.springframework.stereotype.Component;
import su35.setec.reportingservice.dto.response.ReportStoredResponse;
import su35.setec.reportingservice.entity.Report;

@Component
public class ReportMapper {

    public ReportStoredResponse toStoredResponse(Report report) {
        return ReportStoredResponse.builder()
                .id(report.getId())
                .reportType(report.getReportType().name())
                .generatedAt(report.getGeneratedAt())
                .build();
    }
}
