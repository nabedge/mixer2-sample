package org.mixer2.sample;

import org.mixer2.sample.config.SpringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.showBanner(true);
        // builder.profiles("production");
        builder.sources(SpringConfig.class);
        context = builder.run(args);
        context.registerShutdownHook();
        logger.info("SERVER RUNNING !");
    }

    public static void close() {
        if (context != null) {
            context.close();
        }
    }
}
