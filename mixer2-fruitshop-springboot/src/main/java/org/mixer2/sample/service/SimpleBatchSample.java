package org.mixer2.sample.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleBatchSample {

    private Logger logger = Logger.getLogger(SimpleBatchSample.class);

    /**
     * This method will be execute automatically. 30 second interval
     * while web application is running.
     * 
     * You can also use UNIX like cron.
     * See http://static.springsource.org/spring/docs/3.0.x/reference/scheduling.html
     */
    @Scheduled(fixedDelay = 30000)
    public void fooBar() {
        Date date = new Date();
        logger.info("scheduled fooBar() method executed. " + date.toString());
    }

}
