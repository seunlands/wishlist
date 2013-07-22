package i2.application.extranet.action.selectionAutoEcole;

import i2.application.aurige.bean.AutoEcole;
import i2.application.aurige.bean.Departement;
import i2.application.aurige.bean.TypeEtablissement;
import i2.application.extranet.bean.view.ViewAE;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.business.authentification.IAutoEcoleManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.enums.EnumTypeEtablissement;
import i2.application.extranet.utils.Constants;
import i2.application.extranet.utils.WebBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class SelectionAutoEcoleController extends MultiActionController {

    private final static Logger LOGGER = Logger.getLogger(SelectionAutoEcoleController.class);

    private WebBundle messages;

    private IAutoEcoleManager autoEcoleManager;

    private String viewName;

    private String forwardViewName;

    public WebBundle getMessages() {
	return messages;
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();
	ModelAndView modelAndView = new ModelAndView(viewName, Constants.MODELE, map);
	String textPath = (String) request.getSession().getAttribute(Constants.SES_PATH_TEXTE);
	// FIXME Lors de merge avec mode texte la verification de textPath != null n'est pas necessaire
	boolean isTextMode = (textPath != null && textPath.equals("/txt"));
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() != null && (userConnected.getRestriction() instanceof AutoEcole)) {
	    if (isTextMode || userConnected.getLibelleProfil().equals(EnumProfils.AE.getLibelle()))
		modelAndView.setViewName("redirect:" + forwardViewName);
	    else if ((userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle()) || userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()))) {
		Departement dep = userConnected.getDepartement();
		List<AutoEcole> listeAE = autoEcoleManager.getAutoEcoleValideByDepartement(dep);
		List<ViewAE> listViewAe = new ArrayList<>();
		for (AutoEcole ae : listeAE)
		    listViewAe.add(transformBeanAEToViewAE(ae));
		map.put(Constants.LIST_AE, listViewAe);
	    }
	} else {
	    // sendUnauthorized("init", map, userConnected);
	}
	return modelAndView;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, ViewAE command) throws Exception {
	// Map<String, Object> map = new HashMap<String, Object>();
	// String id = null;
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() != null && (userConnected.getRestriction() instanceof AutoEcole)
		&& (userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle()) || userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()))) {
	    AutoEcole ae = autoEcoleManager.readAutoEcole(command.getAueCode());
	    Departement dep = userConnected.getDepartement();
	    List<AutoEcole> listeAE = autoEcoleManager.getAutoEcoleValideByDepartement(dep);
	    for (AutoEcole ecole : listeAE) {
		if (ecole.getAueCode().equals(ecole.getAueCode()))
		    request.getSession().setAttribute(Constants.SES_CHOIX_AE_RATTACH, ae);
	    }
	} else {
	    // sendUnauthorized("init", map, userConnected);
	}
	return new ModelAndView("redirect:" + forwardViewName);

    }

    private ViewAE transformBeanAEToViewAE(AutoEcole ecole) {
	ViewAE view = new ViewAE();
	if (ecole.getAueCodeRafael().equals("")) {
	    view.setAueCodeRafael("Indéfini");
	    LOGGER.debug("... Le code rafael de l'école " + ecole.getAueRaisonSociale() + " est indéfin ");
	} else {
	    view.setAueCodeRafael(ecole.getAueCodeRafael());
	    LOGGER.debug("... Le code rafael de l'école est : " + ecole.getAueCodeRafael());
	}
	view.setRaisonSociale(ecole.getAueRaisonSociale());
	view.setAueCode(ecole.getAueCode());
	view.setAueCodePostal(ecole.getAueCodePostal());
	TypeEtablissement typeEtab = ecole.getTypeEtab();
	if (typeEtab.getTypeId() == EnumTypeEtablissement.LIMITROPHE.getType() || typeEtab.getTypeId() == EnumTypeEtablissement.DIXMILLE.getType())
	    view.setTypeEtab(ecole.getTypeEtab().getTypeLibelle());
	else
	    view.setTypeEtab("");
	return view;
    }

    public void setAutoEcoleManager(IAutoEcoleManager autoEcoleManager) {
	this.autoEcoleManager = autoEcoleManager;
    }

    public String getViewName() {
	return viewName;
    }

    public void setViewName(String viewName) {
	this.viewName = viewName;
    }

    public String getForwardViewName() {
	return forwardViewName;
    }

    public void setForwardViewName(String forwardViewName) {
	this.forwardViewName = forwardViewName;
    }

    public void setMessages(WebBundle messages) {
	this.messages = messages;
    }
}
