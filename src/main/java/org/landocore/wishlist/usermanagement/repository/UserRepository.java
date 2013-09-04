package org.landocore.wishlist.usermanagement.repository;

import org.landocore.wishlist.common.repository.AbstractDao;
import org.landocore.wishlist.usermanagement.domain.User;

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
     * @param pUserId the user's id to search for
     * @return a user
     */
    User findById(Long pUserId);

    /**
     * find the user by email.
     * @param pEmail the user's email to search for
     * @return a user
     */
    User findByEmail(String pEmail);

}
