package org.landocore.wishlist.web.action.login;

import org.apache.log4j.Logger;
import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.business.login.UserLoginService;
import org.landocore.wishlist.business.user.UserService;
import org.landocore.wishlist.web.form.login.ForgottenPasswordForm;
import org.landocore.wishlist.web.form.login.NewAccountForm;
import org.landocore.wishlist.web.utils.LibelleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 09:03
 * Controller for the actions of user management
 */

@Controller
@RequestMapping("/auth")
public class LoginLogoutController {

    /**
     * the logger.
     */
    private static Logger logger = Logger.
            getLogger(LoginLogoutController.class);

    /**
     * injection dependency spring userLoginService.
     */
    private UserLoginService userLoginService;

    /**
     * setter of userLoginService.
     * @param pUserLoginService the uses login service to use
     */
    @Autowired
    public final void setUserLoginService(
            final UserLoginService pUserLoginService) {
        this.userLoginService = pUserLoginService;
    }

    /**
     * the mailsender.
     */
    private MailSender mailSender;

    /**
     * sete of the mailsender.
     * @param pMailSender mail sender to use
     */
    @Autowired
    public final void setMailSender(final MailSender pMailSender) {
        this.mailSender = pMailSender;
    }

    /**
     * the mail template.
     */
    private SimpleMailMessage templateMessage;

    /**
     * setter of the mail template.
     * @param pTemplateMessage SimpleMailMessage template to use
     */
    @Autowired
    public final void setTemplateMessage(
            final SimpleMailMessage pTemplateMessage) {
        this.templateMessage = pTemplateMessage;
    }

    /**
     * the spring dep userService.
     */
    private UserService userService;

    /**
     * setter of the userService.
     * @param pUserService the user service to use
     */
    @Autowired
    public final void setUserService(final UserService pUserService) {
        this.userService = pUserService;
    }

    /**
     * resource bundle regarding the emails.
     */
    private ResourceBundle emailBundle;

    /**
     * setter of the emailBundle.
     * @param pEmailBundle the ResourceBundle to use for email
     */
    @Autowired
    public final void setEmailBundle(final ResourceBundle pEmailBundle) {
        this.emailBundle = pEmailBundle;
    }

    /**
     * Request for login page.
     * @param error request parameter error
     * @param model model and map of the view
     * @return the view name
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public final String getLoginPage(
            @RequestParam(
                    value = "error", required = false) final boolean error,
            ModelMap model) {
        logger.debug("Received request to show login page");

        if (error) {
            model.put("error", "You have enter invalid credentials");
        } else {
            model.put("error", "");
        }

        return "/loginpage";
    }


    /**
     * Request to show the denied access page.
     * @return view of the denied access page
     */
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public final String getDeniedPage() {
        logger.debug("Received request to show denied page");
        return "/deniedpage";
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
        logger.debug("Received request to reset password for user "
                + forgottenPasswordForm.getUsername());

        String password = userLoginService.
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
            logger.debug("user " + forgottenPasswordForm.getUsername()
                    + " not found");
        }

        model.put("submitted", true);
        return "/password";
    }


    /**
     * Request for new password page.
     * @param model the model and view
     * @return the view
     */
    @RequestMapping("/forgottenpassword")
    public final String getForgottenPasswordPage(ModelMap model) {
        model.put("command", new ForgottenPasswordForm());
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
        userService.createUser(user);
        model.put("message", "Account created");
        return "/loginpage";
    }


}
