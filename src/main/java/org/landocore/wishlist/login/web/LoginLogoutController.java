package org.landocore.wishlist.login.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

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

    /**
     * forward the login to spring security.
     * @return NULL
     * @throws ServletException dispatcher forward
     * @throws IOException dispatcher forward
     */
    public final String doLogin() throws ServletException, IOException {
        //authentication manager located in  Spring config
        AuthenticationManager authenticationManager =
                (AuthenticationManager) getSpringBean("authenticationManager");
        //simple token holder
        Authentication authenticationRequestToken =
                createAuthenticationToken(loginFormBackingBean);
        //authentication action
        try {
            Authentication authenticationResponseToken =
                    authenticationManager.authenticate(authenticationRequestToken);
            SecurityContextHolder.getContext().setAuthentication(authenticationResponseToken);
            //ok, test if authenticated, if yes reroute
            if (authenticationResponseToken.isAuthenticated()) {
                //lookup authentication success url, or find redirect parameter from login bean
                return "/secure/examples";
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


    /**
     * create authnetication token from login form.
     * @param loginFormBean  login form
     * @return  authentication token
     */
    private Authentication createAuthenticationToken(
            final LoginFormBackingBean loginFormBean) {
        UsernamePasswordAuthenticationToken usernamePwdAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginFormBean.getUserName(),
                        loginFormBean.getPassword()
                );
        return usernamePwdAuthenticationToken;
    }


    /**
     * gets the srping bean.
     * @param name  name of the bean
     * @return the bean
     */
    private Object getSpringBean(final String name) {
        WebApplicationContext ctx = WebApplicationContextUtils.
                getRequiredWebApplicationContext(
                        (ServletContext) FacesContext.getCurrentInstance().
                                getExternalContext().getContext());
        return ctx.getBean(name);
    }

    /**
     * setter of the login form.
     * @param pLoginFormBackingBean  the login form
     */
    public final void setLoginFormBackingBean(
            final LoginFormBackingBean pLoginFormBackingBean) {
        this.loginFormBackingBean = pLoginFormBackingBean;
    }

}
