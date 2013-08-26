package org.landocore.wishlist.usermanagement.web;

import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.common.utils.LibelleUtil;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 25/08/13
 * Time: 20:43
 * MVC controller to control the user management.
 */

@Controller
@RequestMapping("/user")
public class UserMgmtController {

    /**
     * the SLF4J logger.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(UserMgmtController.class);

    /**
     * the spring dep userService.
     */
    @Autowired
    private UserService userService;


    /**
     * the mailsender.
     */
    @Autowired
    private MailSender mailSender;

    /**
     * the mail template.
     */
    @Autowired
    private SimpleMailMessage templateMessage;

    /**
     * resource bundle regarding the emails.
     */
    @Autowired
    private ResourceBundle emailBundle;




    /**
     * Request for new password page.
     * @param model the model and view
     * @return the view
     */
    @RequestMapping("/forgottenpassword")
    public final String getForgottenPasswordPage(ModelMap model) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request to reset password page");
        }
        model.put("command", new ForgottenPasswordForm());
        model.remove("msg");
        return "/password";
    }


    /**
     * Submit form for new password request.
     * @param forgottenPasswordForm the forgottenPassword from
     * @param result binding errors
     * @param model the model and view
     * @return the view
     */
    @RequestMapping(value = "/passwordsubmit", method = RequestMethod.POST)
    public final String getForgottenPasswordSubmit(
            @ModelAttribute("forgottenPasswordForm")
            final ForgottenPasswordForm forgottenPasswordForm,
            final BindingResult result, ModelMap model) {

        LOGGER.debug("Received request to reset password for user "
                + forgottenPasswordForm.getUsername());

        String password = userService.
                resetPassword(forgottenPasswordForm.getUsername());
        if (password != null) {
            SimpleMailMessage message =
                    new SimpleMailMessage(this.templateMessage);
            User user = userService.getUserByUsername(
                    forgottenPasswordForm.getUsername());
            message.setTo(user.getEmail());
            String templateFilename = emailBundle.
                    getString("email.template.password.reset");
            InputStream is = this.getClass().getClassLoader().
                    getResourceAsStream(templateFilename);
            String body = LibelleUtil.getString(
                    is, new Object[]{user.getUsername(), password});
            message.setText(body);
            this.mailSender.send(message);

        } else {
            LOGGER.debug("user " + forgottenPasswordForm.getUsername()
                    + " not found");
        }

        model.put("submitted", true);
        return "/password";
    }


    /**
     * Request for new account.
     * @param model model and view
     * @return the view
     */
    @RequestMapping("/createaccount")
    public final String getNewAccountPage(ModelMap model) {
        model.put("command", new NewAccountForm());
        return "/newaccount";
    }

    /**
     * Submit the new account form.
     * @param newAccountForm new account form
     * @param result binding errors
     * @param model model
     * @return the view
     */
    @RequestMapping("/accountsubmit")
    public final String createAccount(
            @ModelAttribute("newAccountForm")
            final NewAccountForm newAccountForm,
            final BindingResult result, ModelMap model) {
        User user = new User(newAccountForm.getUsername(),
                newAccountForm.getEmail(), newAccountForm.getPassword());
        try {
            userService.createUser(user);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("account for user " + user.getUsername()
                        + " created");
            }
            model.put("message", "Account created");
        } catch (IncompleteUserException e) {
            LOGGER.warn("Account creation for user " + user.getUsername()
                    + " failed. Reason : " + e.getMessage());
            model.put("message", "Account creation failed");
        }
        return "/loginpage";
    }



    //-----------------setter and getters
    /**
     * setter of the userService.
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

}
