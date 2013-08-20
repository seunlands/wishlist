package org.landocore.wishlist.userManagement.web;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 01/08/13
 * Time: 21:50
 * Form to create a new account.
 */
public class NewAccountForm {

    /**
     * the user name.
     */
    private String username;

    /**
     * the password.
     */
    private String password;

    /**
     * the email.
     */
    private String email;

    /**
     * getter username.
     * @return String : the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * setter username.
     * @param pUsername : String the username
     */
    public final void setUsername(final String pUsername) {
        this.username = pUsername;
    }

    /**
     * getter of the password.
     * @return String password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * setter of the password.
     * @param pPassword : String the password
     */
    public final void setPassword(final String pPassword) {
        this.password = pPassword;
    }

    /**
     * getter of the email.
     * @return String the email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * setter of the email.
     * @param pEmail String the email
     */
    public final void setEmail(final String pEmail) {
        this.email = pEmail;
    }
}
