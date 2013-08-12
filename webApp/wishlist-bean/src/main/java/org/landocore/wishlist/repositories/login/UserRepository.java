package org.landocore.wishlist.repositories.login;

import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.repositories.AbstractDao;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:51
 * Interface for user repository.
 */
public interface UserRepository extends AbstractDao<User, Long> {

    /**
     * Return the user by the username.
     * @param pUsername the user's username to search for
     * @return the User corresponding to the login provided
     */
    User findByLogin(String pUsername);

    /**
     * Saves the user.
     * @param pUser user to save
     * @return the saved user
     */
    User save(User pUser);

    /**
     * find the user by id.
     * @param userId the user's id to search for
     * @return
     */
    User findById(Long userId);

    /**
     * find the user by email.
     * @param email
     * @return
     */
    User findByEmail(String email);

}
