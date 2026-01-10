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
    public void recover(){
        if (isInCooldown()) {
            log.warn("SKIPPING RECOVERY: System is in cooldown period.");
            return;
        }
        log.warn("FAILURE DETECTED. Initiating Self-Healing Protocol...");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("docker", "restart", "monitored-app");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("SELF-HEALING SUCCESS: The application has been restarted.");
            }else{
                log.error("RECOVERY FAILED: Docker command failed with exit code {}", exitCode);
            }
        } catch (Exception e) {
            log.error("RECOVERY FAILED: System error", e);
        }
    }

    public boolean isInCooldown(){
        long currentTime = Instant.now().getEpochSecond();
        long timePassed = currentTime - lastRecoveryTime.get();
        return timePassed < COOLDOWN_PERIOD_SECONDS;

    }
}
