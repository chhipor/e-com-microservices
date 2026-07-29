package su35.setec.reportingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su35.setec.reportingservice.dto.response.CustomerReportResponse;
import su35.setec.reportingservice.dto.response.OrderReportResponse;
import su35.setec.reportingservice.dto.response.ProductReportResponse;
import su35.setec.reportingservice.dto.response.ReportStoredResponse;
import su35.setec.reportingservice.dto.response.SalesReportResponse;
import su35.setec.reportingservice.service.ReportService;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/products")
    public ResponseEntity<ProductReportResponse> products() {
        return ResponseEntity.ok(reportService.getProductReport());
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderReportResponse> orders() {
        return ResponseEntity.ok(reportService.getOrderReport());
    }

    @GetMapping("/customers")
    public ResponseEntity<CustomerReportResponse> customers() {
        return ResponseEntity.ok(reportService.getCustomerReport());
    }

    @GetMapping("/sales")
    public ResponseEntity<SalesReportResponse> sales() {
        return ResponseEntity.ok(reportService.getSalesReport());
    }

    @PostMapping("/products/store")
    public ResponseEntity<ReportStoredResponse> storeProducts() {
        return ResponseEntity.ok(reportService.storeProductReport());
    }

    @PostMapping("/orders/store")
    public ResponseEntity<ReportStoredResponse> storeOrders() {
        return ResponseEntity.ok(reportService.storeOrderReport());
    }

    @PostMapping("/customers/store")
    public ResponseEntity<ReportStoredResponse> storeCustomers() {
        return ResponseEntity.ok(reportService.storeCustomerReport());
    }

    @PostMapping("/sales/store")
    public ResponseEntity<ReportStoredResponse> storeSales() {
        return ResponseEntity.ok(reportService.storeSalesReport());
    }
}
