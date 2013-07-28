package org.landocore.wishlist.repositories.login;

import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.repositories.AbstractDao;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:51
 * To change this template use File | Settings | File Templates.
 */
public interface UserRepository extends AbstractDao<User, Long>{

    User findByLogin(String login);
    User save(User user);
    User findById(Long userId);
    User findByEmail(String email);
    User findByLoginOpenId(String loginOpenId);
    User findByFacebookId(String facebookId);
}
