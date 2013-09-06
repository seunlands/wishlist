package org.landocore.wishlist.login.web;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import org.landocore.wishlist.login.service.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 09:03
 * Controller for the actions of user management
 */

@ManagedBean
@RequestScoped
public class LoginLogoutController {

    /**
     * the LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(LoginLogoutController.class);

    /**
     * the login form backing bean.
     */
    @ManagedProperty(value = "#{loginFormBackingBean}")
    private LoginFormBackingBean loginFormBackingBean;
    
    @ManagedProperty(value = "#{userLoginService}")
    private UserLoginService userLoginService;

    /**
     * forward the login to spring security.
     * @return NULL
     * @throws ServletException dispatcher forward
     * @throws IOException dispatcher forward
     */
    public final String doLogin() throws ServletException, IOException {
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received request for login");
        }
        
        try {
        	boolean authenticated = userLoginService.login(loginFormBackingBean.getUserName(), loginFormBackingBean.getPassword());
        	if (authenticated) {
        		return "/wishlist/dashboard";
        	}
        } catch (BadCredentialsException badCredentialsException) {
            FacesMessage facesMessage =
                    new FacesMessage("Login Failed: please check your username/password and try again.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (LockedException lockedException) {
            FacesMessage facesMessage =
                    new FacesMessage("Account Locked: please contact your administrator.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (DisabledException disabledException) {
            FacesMessage facesMessage =
                    new FacesMessage("Account Disabled: please contact your administrator.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

        return null;
    }
    
    
    public final String doLogout() {
    	userLoginService.logout();
    	return "/auth/login";
    }


    /**
     * setter of the login form.
     * @param pLoginFormBackingBean  the login form
     */
    public final void setLoginFormBackingBean(
            final LoginFormBackingBean pLoginFormBackingBean) {
        this.loginFormBackingBean = pLoginFormBackingBean;
    }
    
    /**
     * @param pUserLoginService userLoginService to set
     */
    public final void setUserLoginService(final UserLoginService pUserLoginService) {
    	this.userLoginService = pUserLoginService;
    }

}
