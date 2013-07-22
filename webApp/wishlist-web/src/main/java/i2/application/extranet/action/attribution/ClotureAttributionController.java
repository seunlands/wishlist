package i2.application.extranet.action.attribution;

import i2.application.aurige.bean.AutoEcole;
import i2.application.aurige.bean.Departement;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.business.attribution.IAttributionManager;
import i2.application.extranet.business.attribution.IClotureAttributionManager;
import i2.application.extranet.business.exceptions.DataValidateError;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import csb.common.tools.date.MonthForCalendar;

/**
 * 
 * @author Bull
 * 
 */
public class ClotureAttributionController extends SimpleFormController {
    private final static Logger LOGGER = Logger.getLogger(ClotureAttributionController.class);
    private IClotureAttributionManager clotureAttributionManager;
    private IAttributionManager attributionManager;
    public static String MESSAGE_CONFIRMATION = "";

    public AutoEcole getAutoEcoleConnected(HttpServletRequest request) {
	AutoEcole autoEcoleConnected = null;
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() instanceof AutoEcole) {
	    autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	}
	return autoEcoleConnected;
    }

    public String getMailUserConnected(HttpServletRequest request) {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	return userConnected.getEmail();
    }

    public String getProfilUserConnected(HttpServletRequest request) {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	return userConnected.getLibelleProfil();
    }

    public MonthForCalendar getMoisExamenEditableForAttribution(HttpServletRequest request) {
	AutoEcole autoEcoleConnected = getAutoEcoleConnected(request);
	return attributionManager.getMoisExamenEditableForAttribution(autoEcoleConnected.getDepartement());
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();
	super.referenceData(request);

	Departement depart = getAutoEcoleConnected(request).getDepartement();
	// Le mois pour lequel la clôture sera appliquée
	String anneeMoisToCloture = null;
	String moisExamenFuture = null;

	// Récupérer le mois d'examen Editable, s'il y en a:
	MonthForCalendar moisExamenEditable = getMoisExamenEditableForAttribution(request);
	// S'il y a un moisExamen editable pour attribution:
	if (moisExamenEditable != null) {
	    LOGGER.info("... ... Il y a un mois d'examen ' " + moisExamenEditable.getAnneeMois() + " ' pour lequel la phase d'attribution est ouverte");

	    anneeMoisToCloture = moisExamenEditable.getLibelleMois();

	} else {
	    // Aucun mois pour lequel l'attribution est éditable. Alors, on récupére le mois d'examen qui sera éditable prochainement
	    MonthForCalendar mois = clotureAttributionManager.getMoisExamenFuture(depart, getProfilUserConnected(request));
	    if (mois != null) {
		LOGGER.info("... ... Le mois d'examen qui sera éditable prochainement sera ' " + mois + " '");
		moisExamenFuture = mois.getLibelleMois();
	    }
	}

	// Alimenter la map par les propriétés suivante:
	map.put("moisToCloturer", anneeMoisToCloture);
	map.put("moisFutureToCloturer", moisExamenFuture);

	return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	LOGGER.debug("submit formulaire");

	Map<String, Object> map = new HashMap<String, Object>();
	// filtrer les profils admin et lecteur
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {

	    MonthForCalendar moisExamenEditable = getMoisExamenEditableForAttribution(request);
	    String anneeMoisToClotureFormatBase = null;
	    String anneeMoisToCloture = null;
	    if (moisExamenEditable != null) {
		LOGGER.info("... ... Il y a un mois d'examen ' " + moisExamenEditable.getAnneeMois() + " ' pour lequel la phase d'attribution est ouverte");
		// Préparer le format de AnneeMois (Ex: Juin 2012):
		anneeMoisToCloture = moisExamenEditable.getLibelleMois();
		anneeMoisToClotureFormatBase = moisExamenEditable.getAnneeMois();
	    }
	    // Enregistrement de l'opération
	    try {
		clotureAttributionManager.saveClotureManuelleAttribution(getAutoEcoleConnected(request).getDepartement(), anneeMoisToClotureFormatBase, getMailUserConnected(request));
		map.put("msg", "La clôture manuelle de la phase d'attribution pour le mois " + anneeMoisToCloture + " a bien été enregistrée.");
		LOGGER.info("... ... Enregistrement de la clôture manuelle de la phase d'attribution pour tous le mois");
	    } catch (DataValidateError exception) {
		errors.reject("error", exception.getListMessage().get(0));
		LOGGER.info("... ... Enregistrement impossible car date n'est pas le lendemain de la limite de la validation du TT");

	    } catch (TechnicalError exception) {
		errors.reject("error", exception.getListMessage().get(0));
		LOGGER.info("... ... Enregistrement impossible car impossible de trouver la date limite de la validation du TT");

	    }
	}
	return showForm(request, response, errors, map);
    }

    /**
     * 
     * @param attributionManager
     */
    public void setAttributionManager(IAttributionManager attributionManager) {
	this.attributionManager = attributionManager;
    }

    /**
     * 
     * @param clotureAttributionManager
     */
    public void setClotureAttributionManager(IClotureAttributionManager clotureAttributionManager) {
	this.clotureAttributionManager = clotureAttributionManager;
    }

    /**
     * @return the mESSAGE_CONFIRMATION
     */
    public static String getMESSAGE_CONFIRMATION() {
	return MESSAGE_CONFIRMATION;
    }

    /**
     * @param mESSAGE_CONFIRMATION
     *            the mESSAGE_CONFIRMATION to set
     */
    public static void setMESSAGE_CONFIRMATION(String mESSAGE_CONFIRMATION) {
	MESSAGE_CONFIRMATION = mESSAGE_CONFIRMATION;
    }
}
