package su35.setec.cronservice.service.impl;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public class CronJobState {

    private final AtomicReference<Instant> lastMinuteRun = new AtomicReference<>();
    private final AtomicReference<Instant> lastMidnightRun = new AtomicReference<>();
    private final AtomicReference<Instant> lastSundayRun = new AtomicReference<>();
    private final AtomicReference<UUID> lastMinuteReportId = new AtomicReference<>();
    private final AtomicReference<UUID> lastMidnightReportId = new AtomicReference<>();
    private final AtomicReference<UUID> lastSundayReportId = new AtomicReference<>();

    public Instant getLastMinuteRun() {
        return lastMinuteRun.get();
    }

    public Instant getLastMidnightRun() {
        return lastMidnightRun.get();
    }

    public Instant getLastSundayRun() {
        return lastSundayRun.get();
    }

    public UUID getLastMinuteReportId() {
        return lastMinuteReportId.get();
    }

    public UUID getLastMidnightReportId() {
        return lastMidnightReportId.get();
    }

    public UUID getLastSundayReportId() {
        return lastSundayReportId.get();
    }

    public void markMinuteRun(Instant instant, UUID reportId) {
        lastMinuteRun.set(instant);
        lastMinuteReportId.set(reportId);
    }

    public void markMidnightRun(Instant instant, UUID reportId) {
        lastMidnightRun.set(instant);
        lastMidnightReportId.set(reportId);
    }

    public void markSundayRun(Instant instant, UUID reportId) {
        lastSundayRun.set(instant);
        lastSundayReportId.set(reportId);
    }
}
