package org.landocore.wishlist.login.service.internal;

import org.landocore.wishlist.login.domain.AuthenticationUserDetails;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 26/07/13
 * Time: 23:51
 * User details getter for Spring security
 */
@Service("userDetailsService")
public class AuthenticationUserDetailsGetter implements UserDetailsService {

    /**
     * the LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(AuthenticationUserDetailsGetter .class);

    /**
     * User repo to be used (Spring dep injection).
     */
    private UserRepository userRepository;

    /**
     * setter of the user repo.
     * @param pUserRepository  user repo to be used
     */
    @Autowired
    public final void setUserRepository(final UserRepository pUserRepository) {
        this.userRepository = pUserRepository;
    }

    /**
     * Default constructor.
     */
    public AuthenticationUserDetailsGetter() {

    }

    @Override
    @Transactional
    public final UserDetails loadUserByUsername(final String pUsername) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Load user with username" + pUsername);
        }
        User user = userRepository.findByLogin(pUsername);
        try {
            throwExceptionIfNotFound(user, pUsername);
        } catch (UsernameNotFoundException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Error : Username " + pUsername + " not found");
            }
            throw e;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("User " + pUsername + " found -> returning");
        }
        return new AuthenticationUserDetails(user);
    }

    /**
     * checks if user found.
     * @param pUser user to be checked
     * @param pLogin username to be checked
     */
    private void throwExceptionIfNotFound(final User pUser,
            final String pLogin) {
        if (pUser == null) {
            throw new UsernameNotFoundException("User with login "
                    + pLogin + " has not been found.");
        }
    }
}
