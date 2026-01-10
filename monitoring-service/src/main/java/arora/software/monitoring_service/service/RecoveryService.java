package arora.software.monitoring_service.service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService {
    
    private final Logger log = LoggerFactory.getLogger(RecoveryService.class);
    private final AtomicLong lastRecoveryTime = new AtomicLong(0);
    private static final long COOLDOWN_PERIOD_SECONDS = 20;

    private final AlertService alertService;
    
    public RecoveryService(AlertService alertService){
        this.alertService  = alertService;
    }
    public void recover(){
        if (isInCooldown()) {
            log.warn("SKIPPING RECOVERY: System is in cooldown period.");
            return;
        }
        log.warn("FAILURE DETECTED. Initiating Self-Healing Protocol...");
        alertService.sendAlert(" *CRITICAL ALERT* \nMonitored App is DOWN. \n Initiating Self-Healing protocols...");
        lastRecoveryTime.set(Instant.now().getEpochSecond());

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("docker", "restart", "monitored-app");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("SELF-HEALING SUCCESS: The application has been restarted.");
                alertService.sendAlert("*RECOVERY SUCCESS* \nThe application has been restarted successfully. \nSystem is now stabilizing.");
            }else{
                log.error("RECOVERY FAILED: Docker command failed with exit code {}", exitCode);
                alertService.sendAlert(" *RECOVERY FAILED* \nDocker exit code: " + exitCode + ". \n REQUIRES HUMAN INTERVENTION.");
            }
        } catch (Exception e) {
            log.error("RECOVERY FAILED: System error", e);
            alertService.sendAlert("*SYSTEM ERROR* \nRecovery logic crashed: " + e.getMessage());
        }
    }

    public boolean isInCooldown(){
        long currentTime = Instant.now().getEpochSecond();
        long timePassed = currentTime - lastRecoveryTime.get();
        return timePassed < COOLDOWN_PERIOD_SECONDS;

    }
}
