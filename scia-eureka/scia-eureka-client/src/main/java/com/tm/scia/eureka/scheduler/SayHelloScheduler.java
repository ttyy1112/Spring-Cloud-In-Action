package com.tm.scia.eureka.scheduler;

import com.tm.scia.eureka.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SayHelloScheduler {

    private Logger logger = LoggerFactory.getLogger(SayHelloScheduler.class);
    @Autowired
    private HelloService helloService;

    @Scheduled(cron = "*/1 * * * * ?")
    public void sayHelloPerSecond() {
        logger.info(helloService.sayHello("aTang"));
    }
}
