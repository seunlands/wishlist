package org.landocore.wishlist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.dao.
        DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 23:59
 * Spring security configuration
 */
@Configuration
@ImportResource("/WEB-INF/applicationContextSecurity.xml")
public class SecurityConfig {

    /**
     * the user detail service.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * instantiates spring security filter.
     * @return DelegatingFilterProxy
     */
    @Bean
    public DelegatingFilterProxy springSecurityFilterChain() {
        return new DelegatingFilterProxy();
    }

    /**
     * instantiates the salt source.
     * @return SaltSource
     */
    @Bean
    public SaltSource saltSource() {
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        return saltSource;
    }

    /**
     * Instantiates the password encoder.
     * @return ShaPasswordEncoder
     */
    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder(256);
    }

    /**
     * Instantiates the DaoAuthenticationProvider.
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setSaltSource(saltSource());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * instantiates the Access decision manager.
     * @return AccessDecisionManager
     */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter> lstVoters = new ArrayList<>();
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("ROLE_");
        lstVoters.add(roleVoter);
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        lstVoters.add(authenticatedVoter);
        AffirmativeBased affirmativeBased = new AffirmativeBased(lstVoters);
        return affirmativeBased;

    }
}
