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
     * injection dependency spring userLoginService
     */
    private UserLoginService userLoginService;

    /**
     * setter of userLoginService.
     * @param pUserLoginService
     */
    @Autowired
    public final void setUserLoginService(final UserLoginService pUserLoginService) {
        this.userLoginService = pUserLoginService;
    }

    /**
     * the mailsender
     */
    private MailSender mailSender;

    /**
     * sete of the mailsendre
     * @param pMailSender
     */
    @Autowired
    public final void setMailSender(final MailSender pMailSender) {
        this.mailSender = pMailSender;
    }

    /**
     * the mail template
     */
    private SimpleMailMessage templateMessage;

    /**
     * setter of the mail template
     * @param pTemplateMessage
     */
    @Autowired
    public final void setTemplateMessage(final SimpleMailMessage pTemplateMessage) {
        this.templateMessage = pTemplateMessage;
    }

    /**
     * the spring dep userService
     */
    private UserService userService;

    /**
     * setter of the userService
     * @param pUserService
     */
    @Autowired
    public final void setUserService(final UserService pUserService) {
        this.userService = pUserService;
    }

    /**
     * resource bundle regarding the emails
     */
    private ResourceBundle emailBundle;

    /**
     * setter of the emailBundle
     * @param pEmailBundle
     */
    @Autowired
    public final void setEmailBundle(final ResourceBundle pEmailBundle) {
        this.emailBundle = pEmailBundle;
    }

    /**
     * Request for login page
     * @param error
     * @param model
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public final String getLoginPage(@RequestParam(value = "error", required = false) final boolean error, ModelMap model){
        logger.debug("Received request to show login page");

        if (error){
            model.put("error", "You have enter invalid credentials");
        } else {
            model.put("error", "");
        }

        return "/loginpage";
    }


    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(){
        logger.debug("Received request to show denied page");
        return "/deniedpage";
    }


    @RequestMapping(value = "/passwordsubmit", method = RequestMethod.POST)
    public String getForgottenPasswordSubmit(@ModelAttribute("forgottenPasswordForm") ForgottenPasswordForm forgottenPasswordForm, BindingResult result, ModelMap model){
        logger.debug("Received request to reset password for user " + forgottenPasswordForm.getUsername());

        String password = userLoginService.resetPassword(forgottenPasswordForm.getUsername());
        if (password != null){
            SimpleMailMessage message = new SimpleMailMessage(this.templateMessage);
            User user = userService.getUserByUsername(forgottenPasswordForm.getUsername());
            message.setTo(user.getEmail());
            String templateFilename = emailBundle.getString("email.template.password.reset");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(templateFilename);
            String body = LibelleUtil.getString(is, new Object[]{user.getUsername(), password});
            message.setText(body);
            this.mailSender.send(message);

        } else {
            logger.debug("user " + forgottenPasswordForm.getUsername() + " not found");
        }

        model.put("submitted", true);
        return "/password";
    }


    @RequestMapping("/forgottenpassword")
    public String getForgottenPasswordPage(ModelMap model){
        model.put("command", new ForgottenPasswordForm());
        return "/password";
    }


    @RequestMapping("/createaccount")
    public String getNewAccountPage(ModelMap model){
        model.put("command", new NewAccountForm());
        return("/newaccount");
    }


}
