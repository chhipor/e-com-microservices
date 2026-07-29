package su35.setec.cronservice.service;

import su35.setec.cronservice.dto.response.CronStatusResponse;

public interface CronJobService {

    CronStatusResponse getStatus();
}
