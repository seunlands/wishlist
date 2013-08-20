package org.landocore.wishlist.config;

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
 * User: seun
 * Date: 30/07/13
 * Time: 22:00
 * Application initializer
 */
public class ApplicationInitializer implements WebApplicationInitializer {


    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        //application context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContext.class);
        rootContext.setDisplayName("WishList");

        //add context loader listener
        servletContext.addListener(new ContextLoaderListener(rootContext));

        //declare dispatch servlet
        ServletRegistration.Dynamic dispatcher = servletContext.
                addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("*.do");

        //Register Spring security filter
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.
                addFilter("springSecurityFilterChain",
                        new DelegatingFilterProxy());
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");



    }
}
