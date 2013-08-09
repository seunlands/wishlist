package org.landocore.wishlist.business.user;

import org.landocore.wishlist.beans.login.User;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 19:37
 * Interface for the user service
 */
public interface UserService {

    /**
     * Return a user by the username provided
     * @param username
     * @return
     */
    User getUserByUsername(final String username);

    /**
     * Creates a new user
     * @param user
     * @return
     */
    User createUser(User user);

}
