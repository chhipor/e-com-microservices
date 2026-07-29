package su35.setec.cronservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su35.setec.cronservice.dto.response.CronStatusResponse;
import su35.setec.cronservice.service.CronJobService;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class CronController {

    private final CronJobService cronJobService;

    @GetMapping("/status")
    public ResponseEntity<CronStatusResponse> status() {
        return ResponseEntity.ok(cronJobService.getStatus());
    }
}
