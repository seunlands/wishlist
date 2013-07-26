package org.landocore.wishlist.business.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 26/07/13
 * Time: 23:49
 * To change this template use File | Settings | File Templates.
 */
public interface UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessExpection;
}
