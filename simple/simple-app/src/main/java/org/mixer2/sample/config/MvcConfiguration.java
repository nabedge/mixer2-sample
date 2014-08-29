package org.mixer2.sample.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.valves.CrawlerSessionManagerValve;
import org.mixer2.Mixer2Engine;
import org.mixer2.spring.webmvc.Mixer2XhtmlViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * The SpringMVC application context. <br/>
 * すべての@Controllerクラスがこれでコンポーネントスキャンされる。 <br />
 *
 */
@EnableWebMvc
@Configuration
@ComponentScan(useDefaultFilters = false, basePackages = { "org.mixer2.sample.web.controller" }, //
includeFilters = { @ComponentScan.Filter(Controller.class) })
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private Mixer2Engine mixer2Engine;

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

        // factory.addErrorPages(new ErrorPage(HttpStatus.404,
        // "/notfound.html");
        return factory;
    }

    @Bean
    public SessionTrackingConfigListener sessionTrackingConfigListener() {
        SessionTrackingConfigListener listener = new SessionTrackingConfigListener();
        return listener;
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

    /**
     * Mixer2によるテンプレート処理リゾルバー
     * @return
     */
    @Bean
    public Mixer2XhtmlViewResolver mixer2XhtmlViewResolver() {
        Mixer2XhtmlViewResolver resolver = new Mixer2XhtmlViewResolver();
        resolver.setPrefix("classpath:m2mockup/m2template/");
        resolver.setSuffix(".html");
        resolver.setBasePackage("org.mixer2.sample.web.view");
        resolver.setMixer2Engine(mixer2Engine);
        resolver.setOrder(100);
        return resolver;
    }

    /**
     * JSPによるテンプレート処理リゾルバー
     * @return
     */
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(1000);
        return resolver;
    }

}
