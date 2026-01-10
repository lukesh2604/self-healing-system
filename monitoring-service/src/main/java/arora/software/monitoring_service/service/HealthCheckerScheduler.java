package arora.software.monitoring_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



@Service
public class HealthCheckerScheduler {
    
    private final RestClient restClient;
    private final Logger log = LoggerFactory.getLogger(HealthCheckerScheduler.class);
    private final RecoveryService recoveryService;

    public HealthCheckerScheduler(RestClient.Builder restClientBuilder, @Value("${target.service.url:http://localhost:8080}") String targetUrl, RecoveryService recoveryService){
        this.restClient = restClientBuilder.baseUrl(targetUrl).build();
        this.recoveryService = recoveryService;
    }

    @Scheduled(fixedRate = 5000, initialDelay = 30000)
    public void checkHealth(){
        if (recoveryService.isInCooldown()) {
        log.info("System recovering... Skipping health check.");
        return;
    }
        try {
            restClient.get()
                .uri("/actuator/health")
                .retrieve()
                .body(String.class);
            log.info("Reponse 200 OK system running");
            
        } catch (Exception e) {
            log.error("🚨 Service DOWN: {}", e.getMessage());
            recoveryService.recover();
        }
        
    }

}
