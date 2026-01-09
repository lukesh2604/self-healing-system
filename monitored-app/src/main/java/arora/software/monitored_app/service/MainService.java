package arora.software.monitored_app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MainService {
    
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

        List<byte[]> list = new ArrayList<>();
        while (true) {
            byte[] b = new byte[1048576];
            list.add(b);
    }

}
}
