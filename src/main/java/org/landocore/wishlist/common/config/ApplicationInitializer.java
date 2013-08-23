package org.landocore.wishlist.common.config;

import ch.qos.logback.access.servlet.TeeFilter;
import ch.qos.logback.classic.ViewStatusMessagesServlet;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.
        AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 18/08/13
 * Time: 20:59
 * Spring application initializer
 */
public class ApplicationInitializer implements WebApplicationInitializer {


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        //Setup loggin slf4J
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        //application context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContextSource.class);
        rootContext.setDisplayName("WishList");

        //add context loader listener
        servletContext.addListener(new ContextLoaderListener(rootContext));

        //declare dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.
                addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("*.do");

        //declare logback status servlet
        ServletRegistration.Dynamic viewStatusMessages = servletContext.
                addServlet("viewStatusMessages",
                        new ViewStatusMessagesServlet());
        viewStatusMessages.addMapping("/viewStatusMessages");


        //add filter for Spring Security
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.
                addFilter("springSecurityFilterChain",
                        new DelegatingFilterProxy());
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");


        //add filter for LogBack access
        FilterRegistration.Dynamic teeFilter = servletContext.
                addFilter("teeFilter", new TeeFilter());
        teeFilter.addMappingForUrlPatterns(null, false, "/*");


    }
}
