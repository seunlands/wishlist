package org.landocore.wishlist.usermanagement.web;

import org.landocore.wishlist.common.utils.LibelleUtil;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 27/08/13
 * Time: 15:10
 * Web controller for user management.
 */
@ManagedBean
@RequestScoped
public class ResetPasswordController implements Serializable {

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
     * reset password form.
     */
    @ManagedProperty(value = "#{resetPasswordForm}")
    private ResetPasswordForm resetPasswordForm;

    /**
     * the user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * the mailsender.
     */
    @ManagedProperty(value = "#{mailSender}")
    private MailSender mailSender;

    /**
     * the mail template.
     */
    @ManagedProperty(value = "#{templateMessage}")
    private SimpleMailMessage templateMessage;

    /**
     * resource bundle regarding the emails.
     */
    @ManagedProperty(value = "#{emailBundle}")
    private ResourceBundle emailBundle;



    /**
     * resets the user password.
     */
    public final void resetPassword() {
        String username = this.resetPasswordForm.getUsername();
        FacesContext context = FacesContext.getCurrentInstance();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request received to reset password for user "
                    + username);
        }
        User user = userService.resetPassword(username);
        if (user != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Password for user " + username
                        + " has been reset");
            }
            SimpleMailMessage message =
                    new SimpleMailMessage(this.templateMessage);
            message.setTo(user.getEmail());
            String templateFilename = emailBundle.
                    getString("email.template.password.reset");
            InputStream is = this.getClass().getClassLoader().
                    getResourceAsStream(templateFilename);
            String body = LibelleUtil.getString(
                    is, new Object[]{user.getUsername(), user.getPassword()});
            message.setText(body);
            this.mailSender.send(message);
        } else {
            LOGGER.debug("User " + username
                    + " not found");
        }
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Request done");
        msg.setSummary("You should shortly received an email "
                + "with your password");
        context.addMessage(null, msg);
    }


    //-------------getter and setter

    /**
     * setter of the user service.
     * @param pUserService the user service to use
     */
    public final void setUserService(final UserService pUserService) {
        this.userService = pUserService;
    }

    /**
     * setter of the mailsender.
     * @param pMailSender mail sender to use
     */
    public final void setMailSender(final MailSender pMailSender) {
        this.mailSender = pMailSender;
    }

    /**
     * setter of the mail template.
     * @param pTemplateMessage SimpleMailMessage template to use
     */
    public final void setTemplateMessage(
            final SimpleMailMessage pTemplateMessage) {
        this.templateMessage = pTemplateMessage;
    }

    /**
     * setter of the emailBundle.
     * @param pEmailBundle the ResourceBundle to use for email
     */
    public final void setEmailBundle(final ResourceBundle pEmailBundle) {
        this.emailBundle = pEmailBundle;
    }

    /**
     * setter of the password form
     * @param pResetPasswordForm the reset password form
     */
    public final void setResetPasswordForm(final ResetPasswordForm pResetPasswordForm) {
        this.resetPasswordForm = pResetPasswordForm;
    }
}
