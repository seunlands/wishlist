package org.landocore.wishlist.business.login;

import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.business.authentication.AuthenticationUserDetails;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 27/07/13
 * Time: 00:12
 * To change this template use File | Settings | File Templates.
 */
public interface UserLoginService {

    User getLoggedUser();


    AuthenticationUserDetails getLoggedUserDetails();

    boolean login(Long userId);
    boolean login(String login, String password);
    void logout();
    boolean isLoggedIn();

    String resetPassword(String username);
}
