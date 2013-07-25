package org.landocore.web.action.index;

import org.landocore.wishlist.beans.Personne;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 23/07/13
 * Time: 06:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/init/accueil.do", method = RequestMethod.GET)
    public ModelAndView indexHandler(){
        return new ModelAndView("/index", "user", new Personne());
    }
}
