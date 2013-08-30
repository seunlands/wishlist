package org.landocore.wishlist.usermanagement.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 26/08/13
 * Time: 08:08
 * ManagedBean for JSF.
 */

@ManagedBean(name = "resetPasswordForm")
@SessionScoped
public class ResetPasswordForm implements Serializable {

    /**
     * the Logger.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(ResetPasswordForm.class);

    /**
     * UID.
     */
    private static final long   serialVersionUID = 1L;

    /**
     * the username.
     */
    private String username;





    //----------- Getter and Setter
    /**
     * Getter of the username.
     * @return the username
     */
    public final String getUsername() {
        return this.username;
    }

    /**
     * sets the username.
     * @param pUsername username to set
     */
    public final void setUsername(final String pUsername) {
        this.username = pUsername;
    }
}
