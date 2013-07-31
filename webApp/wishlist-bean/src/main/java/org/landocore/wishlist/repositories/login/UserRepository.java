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
     * @param login
     * @return
     */
    User findByLogin(String login);

    /**
     * Saves the user.
     * @param user
     * @return
     */
    User save(User user);

    /**
     * find the user by id.
     * @param userId
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
