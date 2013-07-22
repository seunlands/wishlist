package i2.application.extranet.action.administration;

import i2.application.extranet.bean.view.administration.ParametreDefaultBean;
import i2.application.extranet.business.administration.IGestionParamManager;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.form.administration.AdministrationForm;
import i2.application.extranet.utils.WebBundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Controleur d'action pour l'ecran de gestion des parametres
 * 
 * @author bull
 * 
 */
public class AdministrationController extends SimpleFormController {

    private final static Logger LOGGER = Logger.getLogger(AdministrationController.class);

    private IGestionParamManager gestionParamManager;

    private WebBundle messages;

    public WebBundle getMessages() {
	return messages;
    }

    public void setMessages(WebBundle messages) {
	this.messages = messages;
    }

    public void setGestionParamManager(IGestionParamManager gestionParamManager) {
	this.gestionParamManager = gestionParamManager;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
	// init du form
	LOGGER.debug("init form administration");
	List<ParametreDefaultBean> params = gestionParamManager.getAllParam();
	AdministrationForm adminForm = new AdministrationForm(params);
	return adminForm;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	LOGGER.debug("submit form");
	// reccup du form administration
	AdministrationForm adminForm = (AdministrationForm) command;
	Map<String, Object> map = new HashMap<String, Object>();
	try {
	    gestionParamManager.updateAllParam(adminForm.getListParametres());
	    map.put("msg", messages.getMessage("msg.common.modifs.enregistrees"));
	} catch (ApplicationException ex) {
	    for (String m : ex.getListMessage()) {
		errors.reject(m, m);
	    }
	}
	List<ParametreDefaultBean> params = gestionParamManager.getAllParam();
	adminForm = new AdministrationForm(params);
	map.put(getCommandName(), adminForm);

	return showForm(request, response, errors, map);
    }

}
