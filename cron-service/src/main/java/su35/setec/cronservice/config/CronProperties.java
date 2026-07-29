package su35.setec.cronservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cron")
public record CronProperties(int archiveRetentionDays) {
}
