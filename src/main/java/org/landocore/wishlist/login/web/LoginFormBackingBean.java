package org.landocore.wishlist.login.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 28/08/13
 * Time: 13:19
 * login backing bean.
 */
@ManagedBean(name = "loginFormBackingBean")
@SessionScoped
public class LoginFormBackingBean {

    /**
     * the username.
     */
    private String userName;

    /**
     * the raw password.
     */
    private String password;


    /**
     * getter of the username.
     * @return the username
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * setter of the username.
     * @param pUserName  the username
     */
    public final void setUserName(final String pUserName) {
        this.userName = pUserName;
    }

    /**
     * getter of the password.
     * @return the password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * setter of the password.
     * @param pPassword  the passaword
     */
    public final void setPassword(final String pPassword) {
        this.password = pPassword;
    }
}
