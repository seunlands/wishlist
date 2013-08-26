package org.landocore.wishlist.login.service.internal;

import org.landocore.wishlist.login.domain.AuthenticationUserDetails;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.landocore.wishlist.login.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.
        RememberMeAuthenticationToken;
import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 06:26
 * Login business class
 */
@Service("userLoginService")
public class SpringSecurityUserLoginService implements UserLoginService {

    /**
     * the user repo (spring dep injection).
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * the authentication manager.
     */
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;


    @Override
    public final User getLoggedUser() {
        User loggedUser = null;
        AuthenticationUserDetails userDetails = getLoggedUserDetails();
        if (userDetails != null) {
            loggedUser = userRepository.findById(userDetails.getId());
        }
        return loggedUser;
    }

    @Override
    public final AuthenticationUserDetails getLoggedUserDetails() {
        AuthenticationUserDetails loggedUserDetails = null;
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthenticationUserDetails) {
                loggedUserDetails = ((AuthenticationUserDetails) principal);
            }
        }
        return loggedUserDetails;
    }

    @Override
    public final boolean login(final Long pUserId) {
        boolean isLoginSuccessful = false;
        User user = userRepository.findById(pUserId);
        if (user != null) {
            AuthenticationUserDetails userDetails =
                    new AuthenticationUserDetails(user);
            String internalHashKeyForAutomaticLoginAfterRegistration
                    = "magicInternalHashKeyForAutomaticLoginAfterRegistration";
            final RememberMeAuthenticationToken rememberMeAuthenticationToken =
                    new RememberMeAuthenticationToken(
                        internalHashKeyForAutomaticLoginAfterRegistration,
                        userDetails, null);
            rememberMeAuthenticationToken.setAuthenticated(true);
            SecurityContextHolder.getContext().
                    setAuthentication(rememberMeAuthenticationToken);
            isLoginSuccessful = true;
        }
        return isLoginSuccessful;

    }

    @Override
    public final boolean login(final String pLogin, final String pPassword) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(pLogin, pPassword));
        boolean isAuthenticated = isAuthenticated(authentication);
        if (isAuthenticated) {
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }
        return isAuthenticated;
    }

    @Override
    public final void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public final boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.
                getContext().getAuthentication();
        return isAuthenticated(authentication);
    }

    /**
     * checks if the user is authenticated.
     * @param pAuthentication authentication context
     * @return true if authenticated
     */
    private boolean isAuthenticated(final Authentication pAuthentication) {
        return  pAuthentication != null
                && !(pAuthentication instanceof AnonymousAuthenticationToken)
                && pAuthentication.isAuthenticated();
    }


    //------Setters and Getters

    /**
     * setter of user repo.
     * @param pUserRepository the user repo to set
     */
    public final void setUserRepository(final UserRepository pUserRepository) {
        this.userRepository = pUserRepository;
    }

    /**
     * setter of authentication manager.
     * @param pAuthenticationManager authentication manager to use.
     */
    public final void setAuthenticationManager(final AuthenticationManager
                                                       pAuthenticationManager) {
        this.authenticationManager = pAuthenticationManager;
    }

}
