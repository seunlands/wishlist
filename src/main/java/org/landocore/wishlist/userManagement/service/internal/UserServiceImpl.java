package org.landocore.wishlist.usermanagement.service.internal;

import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.landocore.wishlist.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private UserRepository userRepository;

    /**
     * password encoder.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public User getUserByUsername(final String username) {
        return userRepository.findByLogin(username);
    }

    @Override
    @Transactional
    public final User createUser(User user) {
        if (user == null) {
            return null;
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userRepository.saveOrUpdate(user);
        return user;
    }



    //-----------------------Setters and Getters
    /**
     * setter of the user repo
     * @param pUserRepository the user repo to set
     */
    public final void setUserRepository(final UserRepository pUserRepository) {
        this.userRepository = pUserRepository;
    }

    /**
     * setter of the Password encoder
     * @param pPasswordEncoder the Password encoder to set
     */
    public final void setPasswordEncoder(final PasswordEncoder pPasswordEncoder) {
        this.passwordEncoder = pPasswordEncoder;
    }



}
