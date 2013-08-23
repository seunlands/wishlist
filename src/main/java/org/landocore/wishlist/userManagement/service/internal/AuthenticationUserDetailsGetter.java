package org.landocore.wishlist.usermanagement.service.internal;

import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public final UserDetails loadUserByUsername(final String pUsername)
            throws UsernameNotFoundException, DataAccessException {
        User user = userRepository.findByLogin(pUsername);
        try {
            throwExceptionIfNotFound(user, pUsername);
        } catch (UsernameNotFoundException e) {
            throw e;
        }
        return new AuthenticationUserDetails(user);
    }

    /**
     * checks if user found.
     * @param pUser user to be checked
     * @param pLogin username to be checked
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException if user not found
     */
    private void throwExceptionIfNotFound(final User pUser, final String pLogin)
            throws UsernameNotFoundException {
        if (pUser == null) {
            throw new UsernameNotFoundException("User with login "
                    + pLogin + " has not been found.");
        }
    }
}
