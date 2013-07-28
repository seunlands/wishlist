package org.landocore.wishlist.web.action.login;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(@RequestParam(value = "error", required = false) boolean error, ModelMap model){
        logger.debug("Received request to show login page");

        if(error == true){
            model.put("error", "You have enter invalid credentials");
        }else{
            model.put("error", "");
        }

        return "/loginpage";
    }


    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(){
        logger.debug("Received request to show denied page");
        return "/deniedpage";
    }


}
