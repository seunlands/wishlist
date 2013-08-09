package org.landocore.wishlist.web.form.login;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 01/08/13
 * Time: 21:50
 * Form to create a new account.
 */
public class NewAccountForm {

    /**
     * the user name
     */
    private String username;

    /**
     * the password
     */
    private String password;

    /**
     * the email
     */
    private String email;

    /**
     * getter username.
     * @return
     */
    public final String getUsername() {
        return username;
    }

    /**
     * setter username.
     * @param pUsername
     */
    public final void setUsername(final String pUsername) {
        this.username = pUsername;
    }

    /**
     * getter of the password.
     * @return
     */
    public final String getPassword() {
        return password;
    }

    /**
     * setter of the password.
     * @param pPassword
     */
    public final void setPassword(final String pPassword) {
        this.password = pPassword;
    }

    /**
     * getter of the email.
     * @return
     */
    public final String getEmail() {
        return email;
    }

    /**
     * setter of the email.
     * @param pEmail
     */
    public final void setEmail(final String pEmail) {
        this.email = pEmail;
    }
}
