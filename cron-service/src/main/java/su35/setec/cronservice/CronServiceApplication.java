package su35.setec.cronservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class CronServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CronServiceApplication.class, args);
    }
}
