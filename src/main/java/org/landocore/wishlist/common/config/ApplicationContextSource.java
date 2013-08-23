package org.landocore.wishlist.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.
        WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 22:22
 * Application context configuration
 */

@Configuration
@ComponentScan(basePackages = { "org.landocore.wishlist" })
@EnableWebMvc
@Import({ DataConfig.class, SecurityConfig.class })
public class ApplicationContextSource extends WebMvcConfigurerAdapter {

    /**
     * the mail sender hostname.
     */
    @Value("${mail.host}")
    private String mailHost;

    /**
     * the mail from address.
     */
    @Value("${mail.from.robot}")
    private String mailFromAdr;

    /**
     * the property placeholder.
     * @return the property placeholder for wishlist.properties
     */
    @Bean
    public static PropertyPlaceholderConfigurer properties() {

        PropertyPlaceholderConfigurer properties =
                new PropertyPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[]
                {new ClassPathResource("wishlist.properties") };
        properties.setLocations(resources);
        properties.setIgnoreUnresolvablePlaceholders(true);
        return properties;
    }

    /**
     * the spring view resolver.
     * @return spring view resolver
     */
    @Bean
    public UrlBasedViewResolver urlBasedViewResolver() {
        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
        urlBasedViewResolver.setViewClass(TilesView.class);
        return urlBasedViewResolver;
    }

    /**
     * instanciate a TilesConfigurer.
     * @return the tilesConfigurer
     */
    @Bean
         public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{"/WEB-INF/tiles.xml"});
        return tilesConfigurer;
    }

    /**
     * Instanciate the mailsender.
     * @return the mail sender
     */
    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        return mailSender;
    }

    /**
     * Instanciate the mail template.
     * @return  SimpleMailMessage
     */
    @Bean
    public SimpleMailMessage templateEmail() {
        SimpleMailMessage template = new SimpleMailMessage();
        template.setFrom(mailFromAdr);
        return template;
    }

    /**
     *  Instanciate the email resourceBundle.
     * @return ResourceBundle related to emails
     */
    @Bean
    public ResourceBundle emailBundle() {
        return ResourceBundle.getBundle("wishlist-emails");
    }

    /**
     * instantiates spring security filter.
     * @return DelegatingFilterProxy
     */
    @Bean
    public DelegatingFilterProxy springSecurityFilterChain() {
        return new DelegatingFilterProxy();
    }


}
