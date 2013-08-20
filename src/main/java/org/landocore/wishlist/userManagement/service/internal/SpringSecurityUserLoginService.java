package org.landocore.wishlist.userManagement.service.internal;

import org.landocore.wishlist.userManagement.domain.User;
import org.landocore.wishlist.userManagement.repository.UserRepository;
import org.landocore.wishlist.userManagement.service.UserLoginService;
import org.landocore.wishlist.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.
        RememberMeAuthenticationToken;
import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;
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
 * Login business class
 */
@Service("userLoginService")
public class SpringSecurityUserLoginService implements UserLoginService {

    /**
     * the Automatic login after registration.
     */
    private final String internalHashKeyForAutomaticLoginAfterRegistration
            = "magicInternalHashKeyForAutomaticLoginAfterRegistration";

    /**
     * the user repo (spring dep injection).
     */
    private UserRepository userRepository;

    /**
     * setter of the user repo.
     * @param pUserRepository the user repo to be used
     */
    @Autowired
    public final void setUserRepository(final UserRepository pUserRepository) {
        this.userRepository = pUserRepository;
    }

    /**
     * the authentication manager.
     */
    private AuthenticationManager authenticationManager;

    /**
     * setter for authentication manager.
     * @param pAuthenticationManager the authentication manager
     */
    @Autowired
    @Qualifier("authenticationManager")
    public final void setAuthenticationManager(
            final AuthenticationManager pAuthenticationManager) {
        this.authenticationManager = pAuthenticationManager;
    }

    /**
     * the salt source for password hashing.
     */
    private SaltSource saltSource;

    /**
     * setter for salt source.
     * @param pSaltSource the salt source
     */
    @Autowired
    public final void setReflectionSaltSource(final SaltSource pSaltSource) {
        this.saltSource = pSaltSource;
    }

    /**
     * the password hasher.
     */
    private PasswordEncoder passwordEncoder;

    /**
     * setter for the password hasher.
     * @param pPasswordEncoder the password hasher
     */
    @Autowired
    public final void setPasswordEncoder(
            final PasswordEncoder pPasswordEncoder) {
        this.passwordEncoder = pPasswordEncoder;
    }

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
            UserDetails userDetails = new AuthenticationUserDetails(user);
            Object salt = saltSource.getSalt(userDetails);
            String password = passwordEncoder.encodePassword(newPassword, salt);
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
