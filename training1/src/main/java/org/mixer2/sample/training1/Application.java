package org.mixer2.sample.training1;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.mixer2.Mixer2Engine;
import org.mixer2.spring.webmvc.Mixer2XhtmlViewResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

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

	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	}

//	@Bean
//	public VelocityConfigurer velocityConfigrer() {
//		VelocityConfigurer vc = new VelocityConfigurer();
//		vc.setResourceLoaderPath("classpath:/templates/");
//		Properties props = new Properties();
//		props.setProperty("input.encoding", "UTF-8");
//		props.setProperty("output.encoding", "UTF-8");
//		vc.setVelocityProperties(props);
//		return vc;
//	}
//
//	@Bean
//	public VelocityViewResolver velocityViewResolver() {
//		VelocityViewResolver resolver = new VelocityViewResolver();
//		resolver.setCache(false);
//		resolver.setSuffix(".vm");
//		resolver.setContentType("text/html; charset=UTF-8");
//		resolver.setDateToolAttribute("dateTool");
//		resolver.setNumberToolAttribute("numberTool");
//		resolver.setOrder(101);
//		return resolver;
//	}
//
//	@Bean
//	public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
//		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//		factory.setUriEncoding("UTF-8");
//		return factory;
//	}

}