package org.landocore.wishlist.web.action.login;

import org.apache.log4j.Logger;
import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.business.login.UserLoginService;
import org.landocore.wishlist.business.user.UserService;
import org.landocore.wishlist.web.form.login.ForgottenPasswordForm;
import org.landocore.wishlist.web.utils.EmailBundle;
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
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 09:03
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/auth")
public class LoginLogoutController {


    private static Logger logger = Logger.getLogger(LoginLogoutController.class);

    //injection dependance spring
    private UserLoginService userLoginService;

    @Autowired
    public void UserLoginService(UserLoginService userLoginService){
        this.userLoginService = userLoginService;
    }

    private MailSender mailSender;

    @Autowired
    public void setMailSender(MailSender mailSender){
        this.mailSender = mailSender;
    }

    private SimpleMailMessage templateMessage;

    @Autowired
    public void setTemplateMessage(SimpleMailMessage templateMessage){
        this.templateMessage = templateMessage;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    private ResourceBundle emailBundle;

    @Autowired
    public void setEmailBundle(ResourceBundle emailBundle){
        this.emailBundle = emailBundle;
    }



    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(@RequestParam(value = "error", required = false) boolean error, ModelMap model){
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


}
