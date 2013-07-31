package org.landocore.wishlist.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@ComponentScan(basePackages = {"org.landocore.wishlist.web", "org.landocore.wishlist.business"})
@EnableWebMvc
@Import({DataConfig.class, SecurityConfig.class})
public class ApplicationContext extends WebMvcConfigurerAdapter {

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.from.robot}")
    private String mailFromAdr;

    @Bean
    public static PropertyPlaceholderConfigurer properties(){

        PropertyPlaceholderConfigurer properties = new PropertyPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[]{new ClassPathResource("wishlist.properties")};
        properties.setLocations(resources);
        properties.setIgnoreUnresolvablePlaceholders(true);
        return properties;
    }

    @Bean
    public UrlBasedViewResolver urlBasedViewResolver(){
        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
        urlBasedViewResolver.setViewClass(TilesView.class);
        return urlBasedViewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer(){
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{"/WEB-INF/tiles.xml"});
        return tilesConfigurer;
    }

    @Bean
    public MailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateEmail(){
        SimpleMailMessage template = new SimpleMailMessage();
        template.setFrom(mailFromAdr);
        return template;
    }

    @Bean
    public ResourceBundle emailBundle(){
        return ResourceBundle.getBundle("wishlist-emails");
    }

}
