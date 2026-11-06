package su35.setec.reportingservice.service.impl;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.reportingservice.dto.response.CustomerReportResponse;
import su35.setec.reportingservice.dto.response.OrderReportResponse;
import su35.setec.reportingservice.dto.response.ProductReportResponse;
import su35.setec.reportingservice.dto.response.ReportStoredResponse;
import su35.setec.reportingservice.dto.response.SalesReportResponse;
import su35.setec.reportingservice.entity.Order;
import su35.setec.reportingservice.entity.OrderItem;
import su35.setec.reportingservice.entity.Report;
import su35.setec.reportingservice.entity.ReportType;
import su35.setec.reportingservice.repository.CustomerRepository;
import su35.setec.reportingservice.repository.OrderRepository;
import su35.setec.reportingservice.repository.ProductRepository;
import su35.setec.reportingservice.repository.ReportRepository;
import su35.setec.reportingservice.mapper.ReportMapper;
import su35.setec.reportingservice.service.ReportService;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductReportResponse getProductReport() {
        List<su35.setec.reportingservice.entity.Product> products = productRepository.findAll();
        BigDecimal averagePrice = products.stream()
                .map(su35.setec.reportingservice.entity.Product::getUnitPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!products.isEmpty()) {
            averagePrice = averagePrice.divide(BigDecimal.valueOf(products.size()), 2, RoundingMode.HALF_UP);
        }
        return ProductReportResponse.builder()
                .totalProducts(products.size())
                .activeProducts(products.stream().filter(su35.setec.reportingservice.entity.Product::isActive).count())
                .totalStockQuantity(products.stream().mapToLong(p -> p.getQuantityInStock()).sum())
                .averagePrice(averagePrice)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderReportResponse getOrderReport() {
        List<Order> orders = orderRepository.findAll();
        BigDecimal totalSales = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageOrderValue = orders.isEmpty()
                ? BigDecimal.ZERO
                : totalSales.divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);

        return OrderReportResponse.builder()
                .totalOrders(orders.size())
                .completedOrders(orders.stream().filter(order -> order.getStatus().name().equals("COMPLETED")).count())
                .cancelledOrders(orders.stream().filter(order -> order.getStatus().name().equals("CANCELLED")).count())
                .totalSales(totalSales)
                .averageOrderValue(averageOrderValue)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerReportResponse getCustomerReport() {
        return CustomerReportResponse.builder()
                .totalCustomers(customerRepository.count())
                .distinctCustomerEmails(customerRepository.findAll().stream().map(c -> c.getEmail()).distinct().count())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReportResponse getSalesReport() {
        List<Order> orders = orderRepository.findAll();
        List<OrderItem> items = orders.stream().flatMap(order -> order.getItems().stream()).toList();
        OrderItem bestSelling = items.stream()
                .max(Comparator.comparingInt(OrderItem::getQuantity))
                .orElse(null);

        return SalesReportResponse.builder()
                .totalRevenue(orders.stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                .totalOrders(orders.size())
                .bestSellingProduct(bestSelling == null ? null : bestSelling.getProduct().getName())
                .bestSellingQuantity(bestSelling == null ? 0 : bestSelling.getQuantity())
                .build();
    }

    @Override
    @Transactional
    public ReportStoredResponse storeProductReport() {
        return store(ReportType.PRODUCTS, getProductReport());
    }

    @Override
    @Transactional
    public ReportStoredResponse storeOrderReport() {
        return store(ReportType.ORDERS, getOrderReport());
    }

    @Override
    @Transactional
    public ReportStoredResponse storeCustomerReport() {
        return store(ReportType.CUSTOMERS, getCustomerReport());
    }

    @Override
    @Transactional
    public ReportStoredResponse storeSalesReport() {
        return store(ReportType.SALES, getSalesReport());
    }

    private ReportStoredResponse store(ReportType type, Object payload) {
        try {
            Report report = Report.builder()
                    .reportType(type)
                    .generatedAt(Instant.now())
                    .payload(objectMapper.writeValueAsString(payload))
                    .build();
            return reportMapper.toStoredResponse(reportRepository.save(report));
        } catch (JacksonException ex) {
            throw new IllegalStateException("Unable to serialize report payload", ex);
        }
    }
}
