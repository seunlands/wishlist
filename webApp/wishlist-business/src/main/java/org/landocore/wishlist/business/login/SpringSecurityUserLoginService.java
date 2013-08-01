package org.landocore.wishlist.business.login;

import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.business.authentication.AuthenticationUserDetails;
import org.landocore.wishlist.business.util.StringUtils;
import org.landocore.wishlist.repositories.login.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 06:26
 * To change this template use File | Settings | File Templates.
 */
@Service("userLoginService")
public class SpringSecurityUserLoginService implements UserLoginService {

    private final String internalHashKeyForAutomaticLoginAfterRegistration = "magicInternalHashKeyForAutomaticLoginAfterRegistration";

    private UserRepository userRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private AuthenticationManager authenticationManager;
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    private SaltSource saltSource;

    @Autowired
    public void setReflectionSaltSource(SaltSource saltSource){
        this.saltSource = saltSource;
    }

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
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
        if (user != null) {
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

    @Override
    @Transactional
    public String resetPassword(String username){
        User user = userRepository.findByLogin(username);
        String newPassword = null;
        if(user!=null){
            newPassword = StringUtils.generateRandomPassword(8);
            UserDetails userDetails = new AuthenticationUserDetails(user);
            Object salt = saltSource.getSalt(userDetails);
            String password = passwordEncoder.encodePassword(newPassword, salt);
            user.setPassword(password);
            userRepository.saveOrUpdate(user);
        }
        return newPassword;
    }

    private boolean isAuthenticated(Authentication authentication){
        return  authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
