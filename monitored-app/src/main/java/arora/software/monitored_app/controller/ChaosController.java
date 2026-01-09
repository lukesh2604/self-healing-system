package arora.software.monitored_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import arora.software.monitored_app.service.MainService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/chaos")
public class ChaosController {

    private final MainService mainService;
    private static final Logger log = LoggerFactory.getLogger(ChaosController.class);

    public ChaosController(MainService mainService){
        this.mainService = mainService;

    }

    @PostMapping("/kill")
    public void kill(){
        log.warn("Chaos intiated: System.exit invoked by user. ");
        this.mainService.kill();
    }

    @PostMapping("/latency")
    public void latency() throws InterruptedException{
        log.warn("Chaos intiated: latency call");
        this.mainService.latency();
    }

    @PostMapping("/exception")
    public void exception(){
        log.warn("Choas intiated: trigering exception");
        this.mainService.triggerException();
    }

    @PostMapping("/memory")
    public void memory(){
        log.warn("Chaos intiated: system out of memory");
        this.mainService.memory();
    }
}
