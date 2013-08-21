package org.landocore.wishlist.userManagement.service.internal;

import org.landocore.wishlist.userManagement.domain.User;
import org.landocore.wishlist.userManagement.repository.UserRepository;
import org.landocore.wishlist.userManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 19:37
 * Business service related to User management
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    /**
     * user repo.
     */
    private UserRepository userRepository;

    /**
     * setter user repo.
     * @param pUserRepository the user repo to use
     */
    @Autowired
    public final void setUserRepository(final UserRepository pUserRepository) {
        this.userRepository = pUserRepository;
    }

    /**
     * saltsource for password encoding.
     */
    private SaltSource saltSource;

    /**
     * setter salt source.
     * @param pSaltSource the salt source to use
     */
    @Autowired
    public final void setReflectionSaltSource(final SaltSource pSaltSource) {
        this.saltSource = pSaltSource;
    }

    /**
     * password encoder.
     */
    private PasswordEncoder passwordEncoder;

    /**
     * setter password encoder.
     * @param pPasswordEncoder the password encoder to use
     */
    @Autowired
    public final void setPasswordEncoder(
            final PasswordEncoder pPasswordEncoder) {
        this.passwordEncoder = pPasswordEncoder;
    }


    @Override
    @Transactional
    public final User getUserByUsername(final String username) {
        return userRepository.findByLogin(username);
    }

    @Override
    @Transactional
    public final User createUser(User user) {
        if (user == null) {
            return null;
        }
        UserDetails userDetails = new AuthenticationUserDetails(user);
        Object salt = saltSource.getSalt(userDetails);
        String password = passwordEncoder.
                encodePassword(user.getPassword(), salt);
        user.setPassword(password);
        userRepository.saveOrUpdate(user);
        return user;
    }

}
