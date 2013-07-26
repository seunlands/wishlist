package org.landocore.wishlist.business.authentication;

import javax.sql.rowset.spi.TransactionalWriter;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 26/07/13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationUserDetailsGetter implements UserDetailsService {

    private UserRepository userRepository;

    protected AuthenticationUserDetailsGetter(){

    }

    public AuthenticationUserDetailsGetter(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException{
        User user = userRepository.findByLogin(username);
        throwExceptionIfNotFound(user, username);
        return new AuthenticationUserDetails(user);
    }

    private void throwExceptionIfNotFound(User user, String login) {
        if(user == null){
            throw new UserNameNotFoundException("User with login " + login + " has not been found.");
        }
    }
}
