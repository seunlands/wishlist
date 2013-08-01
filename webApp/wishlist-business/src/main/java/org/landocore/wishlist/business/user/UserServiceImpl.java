package org.landocore.wishlist.business.user;

import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.repositories.login.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
@Service("userService")
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User getUserByUsername(String username){
        User user = userRepository.findByLogin(username);
        return user;
    }

}
