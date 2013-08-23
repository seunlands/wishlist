package org.landocore.wishlist.usermanagement.service;

import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.service.internal.
        AuthenticationUserDetails;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 27/07/13
 * Time: 00:12
 * User login service interface
 */
public interface UserLoginService {

    /**
     * Get the logged in user.
     * @return a user
     */
    User getLoggedUser();

    /**
     * Get the authentication details from logged in user.
     * @return AuthenticationUserDetails
     */
    AuthenticationUserDetails getLoggedUserDetails();

    /**
     * Logs the user in.
     * @param pUserId the username to login
     * @return true if user logged in successfully
     */
    boolean login(Long pUserId);

    /**
     * Logs the user is.
     * @param pLogin the username
     * @param pPassword the password
     * @return true if login is successful
     */
    boolean login(String pLogin, String pPassword);

    /**
     * logs the current user out.
     */
    void logout();

    /**
     * Checks if a user is logged in.
     * @return true if a user is logged in
     */
    boolean isLoggedIn();

    /**
     * Resets the users password.
     * @param pUsername User's username
     * @return the password
     */
    String resetPassword(String pUsername);
}
