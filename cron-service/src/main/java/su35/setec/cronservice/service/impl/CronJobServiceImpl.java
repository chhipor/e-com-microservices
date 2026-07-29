package su35.setec.cronservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.cronservice.config.CronProperties;
import su35.setec.cronservice.dto.response.CronStatusResponse;
import su35.setec.cronservice.entity.AuditLog;
import su35.setec.cronservice.entity.Order;
import su35.setec.cronservice.entity.OrderItem;
import su35.setec.cronservice.entity.Product;
import su35.setec.cronservice.entity.Report;
import su35.setec.cronservice.entity.ReportType;
import su35.setec.cronservice.repository.AuditLogRepository;
import su35.setec.cronservice.repository.OrderRepository;
import su35.setec.cronservice.repository.ProductRepository;
import su35.setec.cronservice.repository.ReportRepository;
import su35.setec.cronservice.repository.UserRepository;
import su35.setec.cronservice.service.CronJobService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronJobServiceImpl implements CronJobService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReportRepository reportRepository;
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final CronJobState state;
    private final CronProperties cronProperties;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void everyMinute() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("totalUsers", userRepository.count());
        payload.put("totalOrders", orderRepository.count());
        payload.put("totalProducts", productRepository.count());
        payload.put("executedAt", Instant.now().toString());

        Report report = saveReport(ReportType.SYSTEM_SUMMARY, payload);
        state.markMinuteRun(report.getGeneratedAt(), report.getId());
        log.info("Minute summary generated: {}", report.getId());
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void everyMidnight() {
        Map<String, Object> payload = new HashMap<>();
        List<Order> orders = orderRepository.findAll();
        BigDecimal totalSales = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        payload.put("date", Instant.now().toString());
        payload.put("dailySales", totalSales);
        payload.put("orderCount", orders.size());

        Report report = saveReport(ReportType.DAILY_SALES, payload);
        state.markMidnightRun(report.getGeneratedAt(), report.getId());
        log.info("Daily sales report generated: {}", report.getId());
    }

    @Scheduled(cron = "0 0 0 ? * SUN")
    @Transactional
    public void everySunday() {
        Instant threshold = Instant.now().minusSeconds(cronProperties.archiveRetentionDays() * 24L * 60L * 60L);
        List<AuditLog> oldLogs = auditLogRepository.findAllByCreatedAtBeforeAndArchivedFalse(threshold);
        for (AuditLog logEntry : oldLogs) {
            logEntry.setArchived(true);
            logEntry.setArchivedAt(Instant.now());
        }
        auditLogRepository.saveAll(oldLogs);

        Map<String, Object> payload = new HashMap<>();
        payload.put("archivedCount", oldLogs.size());
        payload.put("retentionDays", cronProperties.archiveRetentionDays());

        Report report = saveReport(ReportType.AUDIT_ARCHIVE, payload);
        state.markSundayRun(report.getGeneratedAt(), report.getId());
        log.info("Old audit logs archived: {}", oldLogs.size());
    }

    @Override
    public CronStatusResponse getStatus() {
        return CronStatusResponse.builder()
                .lastMinuteRun(state.getLastMinuteRun())
                .lastMidnightRun(state.getLastMidnightRun())
                .lastSundayRun(state.getLastSundayRun())
                .lastMinuteReportId(state.getLastMinuteReportId())
                .lastMidnightReportId(state.getLastMidnightReportId())
                .lastSundayReportId(state.getLastSundayReportId())
                .build();
    }

    private Report saveReport(ReportType type, Object payload) {
        try {
            return reportRepository.save(Report.builder()
                    .reportType(type)
                    .generatedAt(Instant.now())
                    .payload(objectMapper.writeValueAsString(payload))
                    .build());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize report payload", ex);
        }
    }
}
