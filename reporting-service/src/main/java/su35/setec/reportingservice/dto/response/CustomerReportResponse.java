package su35.setec.reportingservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerReportResponse {

    private long totalCustomers;
    private long distinctCustomerEmails;
}
