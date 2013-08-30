package org.landocore.wishlist.common.config;

import ch.qos.logback.access.servlet.TeeFilter;
import ch.qos.logback.classic.ViewStatusMessagesServlet;
import com.sun.faces.config.ConfigureListener;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.
        AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.faces.webapp.FacesServlet;
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
        ServletRegistration.Dynamic facesServlet = servletContext.
                addServlet("facecServlet", new FacesServlet());
        facesServlet.setLoadOnStartup(1);
        facesServlet.addMapping("/faces/*");
        facesServlet.addMapping("*.jsf");
        facesServlet.addMapping("*.faces");
        facesServlet.addMapping("*.xhtml");
        facesServlet.addMapping("/j_spring_security_check");



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
