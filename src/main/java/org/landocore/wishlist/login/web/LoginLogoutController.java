package org.landocore.wishlist.login.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Controller for the actions of user management
 */

@Controller
@RequestMapping("/auth")
@SuppressWarnings("SameReturnValue")
public class LoginLogoutController {

    /**
     * the LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(LoginLogoutController.class);

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
        LOGGER.debug("Received request to show login page");

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
        LOGGER.debug("Received request to show denied page");
        return "/deniedpage";
    }

}
