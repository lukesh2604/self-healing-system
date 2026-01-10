package arora.software.monitoring_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService {
    
    private final Logger log = LoggerFactory.getLogger(RecoveryService.class);

    public void recover(){
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
}
