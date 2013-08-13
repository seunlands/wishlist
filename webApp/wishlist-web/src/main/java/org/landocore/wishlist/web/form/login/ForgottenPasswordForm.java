package org.landocore.wishlist.web.form.login;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 19:05
 * Spring form for password reset.
 */
public class ForgottenPasswordForm {

    /**
     * the username.
     */
    private String username;

    /**
     * getter of the username.
     * @return String : the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * setter of the username.
     * @param pUsername : String the username
     */
    public final void setUsername(final String pUsername) {
        this.username = pUsername;
    }
}
