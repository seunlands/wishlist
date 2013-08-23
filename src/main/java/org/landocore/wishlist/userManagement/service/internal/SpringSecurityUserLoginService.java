package org.landocore.wishlist.usermanagement.service.internal;

import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.landocore.wishlist.usermanagement.service.UserLoginService;
import org.landocore.wishlist.common.utils.StringUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * the password hasher.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;




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

    @Override
    @Transactional
    public final String resetPassword(final String pUsername) {
        User user = userRepository.findByLogin(pUsername);
        String newPassword = null;
        if (user != null) {
            newPassword = StringUtils.generateRandomPassword(8);
            String password = passwordEncoder.encode(newPassword);
            user.setPassword(password);
            userRepository.saveOrUpdate(user);
        }
        return newPassword;
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
}
