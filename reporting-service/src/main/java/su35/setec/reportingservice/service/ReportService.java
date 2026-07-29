package su35.setec.reportingservice.service;

import su35.setec.reportingservice.dto.response.CustomerReportResponse;
import su35.setec.reportingservice.dto.response.OrderReportResponse;
import su35.setec.reportingservice.dto.response.ProductReportResponse;
import su35.setec.reportingservice.dto.response.ReportStoredResponse;
import su35.setec.reportingservice.dto.response.SalesReportResponse;

public interface ReportService {

    ProductReportResponse getProductReport();

    OrderReportResponse getOrderReport();

    CustomerReportResponse getCustomerReport();

    SalesReportResponse getSalesReport();

    ReportStoredResponse storeProductReport();

    ReportStoredResponse storeOrderReport();

    ReportStoredResponse storeCustomerReport();

    ReportStoredResponse storeSalesReport();
}
