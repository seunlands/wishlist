package org.landocore.wishlist.web.action.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 23/07/13
 * Time: 06:58
 * Controller of the index page
 */
@Controller
@RequestMapping("/accueil")
public class IndexController {

    /**
     * request received from /accueil/init.do.
     * @return the next view
     */
    @RequestMapping("/init.do")
    public final ModelAndView init() {
        String message = "Hello world !";
        return new ModelAndView("/index", "message", message);
    }

    /**
     * request received for /accueil/admin.do -> to be deleted.
     * @return  the next view
     */
    @RequestMapping("/admin.do")
    public final ModelAndView admin() {
        return new ModelAndView("/admin", "message", "admin");
    }
}
