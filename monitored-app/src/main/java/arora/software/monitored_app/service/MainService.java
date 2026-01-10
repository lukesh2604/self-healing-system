package arora.software.monitored_app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MainService {
    private final List<byte[]> list = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(MainService.class);
    
    public void kill(){
        System.exit(1);
    }

    public void latency() throws InterruptedException{
        Thread.sleep(10000);
    }

    public void triggerException(){
        throw new RuntimeException("Runtime exception");
    }

    public void memory(){
        int count = 0;
        
        while (true) {
            byte[] b = new byte[50 * 1024 * 1024]; 
            list.add(b);
            
            count++;
            log.info("Allocated chunk #{}. Total Leak: {} MB", count, count * 50);
            
            try {
                // Sleep 100ms to see the logs 
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }
}
