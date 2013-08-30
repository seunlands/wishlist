package org.landocore.wishlist.usermanagement.service;


import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.usermanagement.domain.User;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 19:37
 * Interface for the user service
 */
public interface UserService {

    /**
     * Return a user by the username provided.
     * @param pUsername the user's username to search for
     * @return User if found, else null
     */
    User getUserByUsername(final String pUsername);

    /**
     * Creates a new user.
     * With by default the ROLE_USER -> to be implemented still
     * @param pUser user to create
     * @return the created user
     * @throws IncompleteUserException when user's password is NULL
     */
    User createUser(User pUser) throws IncompleteUserException;

    /**
     * Resets the users password.
     * @param pUsername User's username
     * @return the user with raw password
     */
    User resetPassword(String pUsername);


}
