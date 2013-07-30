package org.landocore.wishlist.web.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@ComponentScan(basePackages = {"org.landocore.wishlist.web"})
@EnableWebMvc
@Import({DataConfig.class, SecurityConfig.class})
public class ApplicationContext extends WebMvcConfigurerAdapter {

    @Bean
    public static DriverManagerDataSource dataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.
    }


    @Bean
    public static PropertyPlaceholderConfigurer properties(){

        PropertyPlaceholderConfigurer properties = new PropertyPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[]{new ClassPathResource("wishlist.properties")};
        properties.setLocations(resources);
        properties.setIgnoreUnresolvablePlaceholders(true);
        return properties;
    }

}
