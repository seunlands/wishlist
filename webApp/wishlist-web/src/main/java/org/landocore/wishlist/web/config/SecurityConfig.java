package org.landocore.wishlist.web.config;

import org.landocore.wishlist.business.authentication.AuthenticationUserDetailsGetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class SecurityConfig  {

    @Bean
    public DelegatingFilterProxy springSecurityFilterChain(){
        DelegatingFilterProxy filterProxy = new DelegatingFilterProxy();
        return filterProxy;
    }

    @Bean
    public SaltSource saltSource(){
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("id");
        return saltSource;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService();

    }
}
