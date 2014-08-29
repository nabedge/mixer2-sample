package org.mixer2.sample;

import org.mixer2.sample.config.MvcConfiguration;
import org.mixer2.sample.config.RootConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class Server {

    private final static Logger logger = LoggerFactory
            .getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.showBanner(true);
        // builder.profiles("production");
        builder.sources( //
                RootConfiguration.class //
                , MvcConfiguration.class //
        );
        builder.run(args);
        logger.info("SERVER RUNNING !");
    }
}
