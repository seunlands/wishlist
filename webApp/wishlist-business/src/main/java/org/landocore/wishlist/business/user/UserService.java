package org.landocore.wishlist.business.user;

import org.landocore.wishlist.beans.login.User;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public interface UserService {

    /**
     * Return a user by the username provided
     * @param username
     * @return
     */
    User getUserByUsername(String username);

}
