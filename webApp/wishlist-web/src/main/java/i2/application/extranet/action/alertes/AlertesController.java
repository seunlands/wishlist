package i2.application.extranet.action.alertes;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.alertes.ViewAlertes;
import i2.application.extranet.business.alertes.IAlertesManager;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.alertes.AlertesForm;
import i2.application.extranet.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * controller de la vue alertes
 * 
 * @author bull
 * 
 */
public class AlertesController extends SimpleFormController {

    private IAlertesManager alertesManager;

    private ResourceBundle messages = ResourceBundle.getBundle("extranet");

    // injecteur
    public void setAlertesManager(IAlertesManager alertesManager) {
	this.alertesManager = alertesManager;
    }

    private final static Logger LOGGER = Logger.getLogger(AlertesController.class);

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
	// init du form
	LOGGER.debug("init form alertes");
	AlertesForm alertesForm = new AlertesForm();
	AutoEcole autoEcoleConnected = null;
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected != null && userConnected.getLibelleProfil().equalsIgnoreCase(EnumProfils.AE.getLibelle()) && userConnected.getRestriction() instanceof AutoEcole) {
	    autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	    alertesForm.setLstAlertes(alertesManager.getLstAlerteForAueByDate(autoEcoleConnected));
	}
	if (userConnected != null
		&& (userConnected.getLibelleProfil().equalsIgnoreCase(EnumProfils.REPARTITEUR.getLibelle()) || userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle()))
		&& userConnected.getRestriction() instanceof AutoEcole) {
	    autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	    alertesForm.setLstAlertes(alertesManager.getLstAlerteForRepByDate(autoEcoleConnected.getDepartement()));
	}
	// reccup nbalertes
	Long nbAlertes = null;
	if (userConnected != null && userConnected.getLibelleProfil().equals(EnumProfils.AE.getLibelle()) && userConnected.getRestriction() instanceof AutoEcole) {
	    nbAlertes = alertesManager.getNbAlertesAue((AutoEcole) userConnected.getRestriction());
	}
	if (userConnected != null && userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()) && userConnected.getRestriction() instanceof AutoEcole) {
	    nbAlertes = alertesManager.getNbAlertesDept(((AutoEcole) userConnected.getRestriction()).getDepartement());
	}
	if (nbAlertes != null) {
	    request.setAttribute(Constants.NB_ALERTES, nbAlertes);
	} else {
	    request.setAttribute(Constants.NB_ALERTES, 0);
	}

	return alertesForm;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	LOGGER.debug("entering onSubmit");
	AlertesForm alertesForm = (AlertesForm) command;
	List<ViewAlertes> lstAlUpd = new ArrayList<ViewAlertes>();
	for (ViewAlertes view : alertesForm.getLstAlertes()) {
	    if (view.isChecked()) {
		lstAlUpd.add(view);
	    }
	}
	Map<String, Object> map = new HashMap<String, Object>();

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	// si une AE
	if (userConnected != null && userConnected.getLibelleProfil().equalsIgnoreCase(EnumProfils.AE.getLibelle())) {
	    if (!lstAlUpd.isEmpty()) {
		try {
		    alertesManager.saveAcquittementsAue(lstAlUpd, (AutoEcole) userConnected.getRestriction());
		    alertesForm.setLstAlertes(alertesManager.getLstAlerteForAueByDate((AutoEcole) userConnected.getRestriction()));
		    map.put("msg", messages.getString("msg.common.modifs.enregistrees"));
		} catch (TechnicalError te) {
		    for (String m : te.getListMessage()) {
			errors.reject(m, m);
		    }
		}
	    }
	}

	if (userConnected != null
		&& (userConnected.getLibelleProfil().equalsIgnoreCase(EnumProfils.REPARTITEUR.getLibelle()) || userConnected.getLibelleProfil().equalsIgnoreCase(EnumProfils.LECTEUR.getLibelle()))) {
	    if (!lstAlUpd.isEmpty()) {
		try {
		    alertesManager.saveAcquittementsDept(lstAlUpd, ((AutoEcole) userConnected.getRestriction()).getDepartement());
		    alertesForm.setLstAlertes(alertesManager.getLstAlerteForRepByDate(((AutoEcole) userConnected.getRestriction()).getDepartement()));
		    map.put("msg", messages.getString("msg.common.modifs.enregistrees"));
		} catch (TechnicalError te) {
		    for (String m : te.getListMessage()) {
			errors.reject(m, m);
		    }
		}
	    }
	}
	map.put(getCommandName(), alertesForm);
	return showForm(request, response, errors, map);
    }
}
