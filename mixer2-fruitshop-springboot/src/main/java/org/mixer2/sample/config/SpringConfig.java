package org.mixer2.sample.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.catalina.valves.CrawlerSessionManagerValve;
import org.mixer2.Mixer2Engine;
import org.mixer2.spring.webmvc.Mixer2XhtmlViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * The SpringMVC application context. <br/>
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = { "org.mixer2.sample" })
public class SpringConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SpringConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        logger.debug("creating datasource...");
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        builder.addScript("classpath:sql/dbinit.sql");
        builder.continueOnError(true);
        return builder.build();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // messageSource.setBasenames("classpath:messages","classpath:messages/validation");
        messageSource.setBasenames("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        // # -1 : never reload, 0 always reload
        // messageSource.setCacheSeconds(0);
        return messageSource;
    }

    @Bean
    public Mixer2Engine mixer2Engine() {
        return Mixer2EngineSingleton.getInstance();
    }

    @Bean
    public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

        factory.setPort(8080);
        factory.setUriEncoding("UTF-8");
        factory.setContextPath("");
        factory.setSessionTimeout(1800, TimeUnit.SECONDS);

        // クローラの大量アクセスがあってもsessionオブジェクトを作りすぎないようにするvalve
        CrawlerSessionManagerValve crawlerSessionManagerValve = new CrawlerSessionManagerValve();
        factory.addContextValves(crawlerSessionManagerValve);

        // factory.addErrorPages(new ErrorPage(HttpStatus.404, "/notfound.html");
        return factory;
    }

    @Bean
    public SessionTrackingConfigListener sessionTrackingConfigListener() {
        SessionTrackingConfigListener listener = new SessionTrackingConfigListener();
        return listener;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }

    @Bean
    public FilterRegistrationBean characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(filter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        filterRegBean.setUrlPatterns(urlPatterns);
        filterRegBean.setOrder(1); // first filter !
        return filterRegBean;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /* For example, Mapping to the login view. */
        // registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m2static/**")//
                .addResourceLocations("classpath:/m2mockup/m2static/")//
                .setCachePeriod(20);
    }

    @Override
    @Bean
    public Validator getValidator() {
        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    @Bean
    public Mixer2XhtmlViewResolver mixer2XhtmlViewResolver() {
        Mixer2XhtmlViewResolver resolver = new Mixer2XhtmlViewResolver();
        resolver.setPrefix("classpath:m2mockup/m2template/");
        resolver.setSuffix(".html");
        resolver.setBasePackage("org.mixer2.sample.web.view");
        resolver.setMixer2Engine(mixer2Engine());
        resolver.setOrder(100);
        return resolver;
    }

//    @Bean
//    public InternalResourceViewResolver internalResourceViewResolver() {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/WEB-INF/jsp/");
//        resolver.setSuffix(".jsp");
//        resolver.setOrder(1000);
//        return resolver;
//    }

}
