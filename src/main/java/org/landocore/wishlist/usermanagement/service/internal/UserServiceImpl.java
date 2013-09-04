package org.landocore.wishlist.usermanagement.service.internal;

import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.common.utils.StringUtils;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.landocore.wishlist.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * the minimum password length.
     */
    @Value("${wishlist.password.min.length}")
    private int passwordLength;




    @Override
    @Transactional
    public final User getUserByUsername(final String username) {
        return userRepository.findByLogin(username);
    }

    @Override
    @Transactional
    public final User createUser(User user) throws IncompleteUserException {
        if (user == null) {
            return null;
        }
        if (user.getPassword() == null) {
            throw new IncompleteUserException("Password is NULL");
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userRepository.saveOrUpdate(user);
        return user;
    }

    @Override
    @Transactional
    public final User resetPassword(final String pUsername) {
        User user = userRepository.findByLogin(pUsername);
        User userToReturn = null;
        String newPassword = null;
        if (user != null) {
            newPassword = StringUtils.generateRandomPassword(passwordLength);
            String password = passwordEncoder.encode(newPassword);
            user.setPassword(password);
            userRepository.saveOrUpdate(user);
            userToReturn = new User(user.getUsername(), user.getEmail(),
            		newPassword);
        }
        return userToReturn;
    }




    //-----------------------Setters and Getters
    /**
     * setter of the user repo.
     * @param pUserRepository the user repo to set
     */
    public final void setUserRepository(final UserRepository pUserRepository) {
        this.userRepository = pUserRepository;
    }

    /**
     * setter of the Password encoder.
     * @param pPasswordEncoder the Password encoder to set
     */
    public final void setPasswordEncoder(final PasswordEncoder
    		pPasswordEncoder) {
        this.passwordEncoder = pPasswordEncoder;
    }

    /**
     * setter of the password length.
     * @param pPasswordLength length og the password
     */
    public final void setPasswordLength(final int pPasswordLength) {
        this.passwordLength = pPasswordLength;
    }





}
