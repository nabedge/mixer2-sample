package org.mixer2.sample.training1;

import org.mixer2.Mixer2Engine;
import org.mixer2.spring.webmvc.Mixer2XhtmlViewResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
    
    @Bean
    public Mixer2Engine mixer2Engine() {
    	return new Mixer2Engine();
    }
    
    @Bean
    public Mixer2XhtmlViewResolver mixer2XhtmlViewResolver() {
        Mixer2XhtmlViewResolver resolver = new Mixer2XhtmlViewResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setBasePackage("org.mixer2.sample.training1.view");
        resolver.setMixer2Engine(mixer2Engine());
        resolver.setOrder(100);
        return resolver;
    }
    
//    @Bean
//    public ControllerClassNameHandlerMapping controllerClassNameHandlerMapping() {
//    	return new ControllerClassNameHandlerMapping();
//    }

}