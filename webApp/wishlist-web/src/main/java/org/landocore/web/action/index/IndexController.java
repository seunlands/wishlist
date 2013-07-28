package org.landocore.web.action.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 23/07/13
 * Time: 06:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/accueil")
public class IndexController {

    @RequestMapping("/init.do")
    public ModelAndView init(){
        String message = "Hello world !";
        return new ModelAndView("/index", "message", message);
    }


    @RequestMapping("/admin.do")
    public ModelAndView admin(){
        return new ModelAndView("/admin", "message", "admin");
    }
}
