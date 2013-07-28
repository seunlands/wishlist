package org.landocore.wishlist.business.login;

import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.business.authentication.AuthenticationUserDetails;
import org.landocore.wishlist.repositories.login.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 06:26
 * To change this template use File | Settings | File Templates.
 */
public class SpringSecurityUserLoginService implements UserLoginService {


    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private final String internalHashKeyForAutomaticLoginAfterRegistration = "magicInternalHashKeyForAutomaticLoginAfterRegistration";

    public SpringSecurityUserLoginService(UserRepository userRepository, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User getLoggedUser() {
        User loggedUser = null;
        AuthenticationUserDetails userDetails = getLoggedUserDetails();
        if(userDetails != null){
            loggedUser = userRepository.findById(userDetails.getId());
        }
        return loggedUser;
    }

    @Override
    public AuthenticationUserDetails getLoggedUserDetails() {
        AuthenticationUserDetails loggedUserDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(isAuthenticated(authentication)){
            Object principal = authentication.getPrincipal();
            if(principal instanceof AuthenticationUserDetails){
                loggedUserDetails = ((AuthenticationUserDetails) principal);
            } else {
                //throw new ThingThatShouldNotBeException("Expected class of authentication principal is AuthenticationUserDetails. Given: " + principal.getClass());
            }
        }
        return loggedUserDetails;
    }

    @Override
    public boolean login(Long userId) {
        boolean isLoginSuccessfull = false;
        User user = userRepository.findById(userId);
        if(user != null){
            AuthenticationUserDetails userDetails = new AuthenticationUserDetails(user);
            final RememberMeAuthenticationToken rememberMeAuthenticationToken = new RememberMeAuthenticationToken(internalHashKeyForAutomaticLoginAfterRegistration, userDetails, null);
            rememberMeAuthenticationToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(rememberMeAuthenticationToken);
            isLoginSuccessfull = true;
        }
        return isLoginSuccessfull;

    }

    @Override
    public boolean login(String login, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        boolean isAuthenticated = isAuthenticated(authentication);
        if(isAuthenticated){
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return isAuthenticated;
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAuthenticated(authentication);
    }

    private boolean isAuthenticated(Authentication authentication){
        return  authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
