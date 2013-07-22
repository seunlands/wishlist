package i2.application.extranet.action.option;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.AbstractPlanningController;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.option.ViewRecapOptionsByPermis;
import i2.application.extranet.business.exceptions.ConcurrentDataAccessException;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.business.option.IOptionManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.option.OptionPoseForm;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;
import i2.application.extranet.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.date.MonthForCalendar;

/**
 * Controller d'action pour le tableau de la pose d'otpions
 * 
 * @author Bull
 * 
 */
public class OptionPoseController extends AbstractPlanningController {

    private final static Logger LOGGER = Logger.getLogger(OptionPoseController.class);

    private IOptionManager optionsPoseManager;

    public void setOptionsPoseManager(IOptionManager optionsPoseManager) {
	this.optionsPoseManager = optionsPoseManager;
    }

    public OptionPoseController() {
	super.setModeAccessibilite(false);
    }

    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	LOGGER.debug("init controller OptionsPoseController");
	ModelAndView mv = super.init(request, response);

	// FIXME comment checker?
	@SuppressWarnings("unchecked")
	Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);
	JSONObject jsonObj = (JSONObject) modele.get(getConstantForJson());

	// Mise à jour du json

	if (jsonObj != null) {
	    String anneeMois = ((JSONObject) jsonObj.get(Constants.MOIS_SELECTION)).getString("id");
	    MonthForCalendar moisExamen = new MonthForCalendar(anneeMois);

	    List<ViewRecapOptionsByPermis> lstRecap = optionsPoseManager.getRecapOptionsByPermis(getAutoEcoleConnected(request), moisExamen.getAnnee(), moisExamen.getMois(), lstLignesBeans);

	    jsonObj = ActionFactory.addRecapOptionsByPermis(jsonObj, lstRecap);
	    Integer dateCloture = optionsPoseManager.getCloture();
	    if (dateCloture != null) {
		request.setAttribute("dateClotureOption", dateCloture);
	    }

	    // Mise à jour du JSON dans le modèle
	    modele.put(getConstantForJson(), jsonObj.toString());
	}
	return mv;
    }

    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, OptionPoseForm command) throws Exception {
	LOGGER.debug("enter submitModification OptionsPoseCOntroller");
	AsyncResponse servletResponse = ActionFactory.createResponse(response);
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	String profilUserConnected = userConnected.getLibelleProfil();
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !profilUserConnected.equals(EnumProfils.LECTEUR.getLibelle())) {
	    if (command.getActId() != 0 && command.getOptionsPose() >= 0) {
		try {
		    AutoEcole ecole = getAutoEcoleConnected(request);
		    String profil = getProfilUserConnected(request);

		    // Traitement lors de l'action recherche Etbs
		    if (command.getAueCodeSelected() != "" && (profil.equals(EnumProfils.REPARTITEUR.getLibelle()) || profil.equals(EnumProfils.LECTEUR.getLibelle()))) {
			ecole = getAutoEcoleManager().readAutoEcole(command.getAueCodeSelected());
		    }
		    if (ecole != null) {
			try {
			    optionsPoseManager.savePositionOptionsByAueByActivite(ecole, userConnected.getEmail(), command.getActId(), command.getOptionsPose(), command.getNbActOptions());
			    servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
			    servletResponse.addMessage(String.valueOf(command.getOptionsPose()));
			    servletResponse.sendSuccess();

			} catch (ConcurrentDataAccessException ex) {
			    servletResponse.addMessage(messages.getMessage("msg.common.modifs.non.enregistrees"));
			    servletResponse.addMessage(messages.getMessage("option.error.concurrence"));
			    servletResponse.sendError();
			}
		    } else {
			servletResponse.addMessage(messages.getMessage("msg.common.modifs.non.enregistrees"));
			servletResponse.addMessage(messages.getMessage("msg.common.aue.not.found"));
			servletResponse.sendFail();

		    }
		} catch (TechnicalError te) {
		    servletResponse.addMessage(messages.getMessage("msg.common.modifs.non.enregistrees"));
		    servletResponse.addMessage(te.getMessage());
		    servletResponse.sendFail();
		}

	    } else {
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.non.enregistrees"));
		servletResponse.sendFail();
	    }
	} else {
	    sendUnauthorized("submitModification", servletResponse, userConnected);
	}
	return null;
    }

    protected void sendUnauthorized(final String action, final AsyncResponse servletResponse, final ViewUtilisateur userConnected) throws IOException {
	servletResponse.addMessage(messages.getMessage("action.non.autorisee"));
	LOGGER.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	servletResponse.sendFail();
    }

}
