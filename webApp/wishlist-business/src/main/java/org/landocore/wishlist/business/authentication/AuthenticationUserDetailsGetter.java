package org.landocore.wishlist.business.authentication;

import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.repositories.login.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.spi.TransactionalWriter;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 26/07/13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
@Service("userDetailsService")
public class AuthenticationUserDetailsGetter implements UserDetailsService {

    private UserRepository userRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public AuthenticationUserDetailsGetter(){

    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userRepository.findByLogin(username);
        throwExceptionIfNotFound(user, username);
        return new AuthenticationUserDetails(user);
    }

    private void throwExceptionIfNotFound(User user, String login) throws UsernameNotFoundException, DataAccessException {
        if(user == null){
            throw new UsernameNotFoundException("User with login " + login + " has not been found.");
        }
    }
}
