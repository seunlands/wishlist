/**
 * 
 */
package i2.application.extranet.action.reservation;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.business.reservation.IAutorisationDroitsManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.reservation.AutorisationDroitsForm;
import i2.application.extranet.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Controleur d'action pour l'ecran d'Autorisation des droits
 * 
 * @author BULL
 * 
 */
public class AutorisationDroitsController extends SimpleFormController {

    private final static Logger LOGGER = Logger.getLogger(AutorisationDroitsController.class);
    private IAutorisationDroitsManager autorisationDroitsManager;
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

    public ViewAnneeMois getListAnneeMoisNotAutoriser(HttpServletRequest request) {
	return autorisationDroitsManager.getListAnneeMoisNotAutoriser(getAutoEcoleConnected(request).getDepartement(), EnumProfils.AE.getLibelle());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();
	super.referenceData(request);

	// init
	ViewAnneeMois moisNonAutorise = getListAnneeMoisNotAutoriser(request);

	map.put("mois", moisNonAutorise);
	map.put("msg", MESSAGE_CONFIRMATION);
	AutorisationDroitsController.setMESSAGE_CONFIRMATION("");
	return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	LOGGER.debug("submit formulaire");
	String anneeMoisSelectionne = ((AutorisationDroitsForm) command).getAnneeMoisSelectionne();

	// filtrer les profils admin et lecteur
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {
	    // Enregistrement de l'opération
	    autorisationDroitsManager.saveValidationAffichageDroits(getAutoEcoleConnected(request).getDepartement(), anneeMoisSelectionne, getMailUserConnected(request));

	    // Préparer le format d'affichage du mois sur lequel on a autorisé l'affichage des droits
	    Calendar cal = new GregorianCalendar(Locale.FRANCE);
	    int annee = Integer.parseInt(anneeMoisSelectionne.substring(0, 4));
	    int mois = Integer.parseInt(anneeMoisSelectionne.substring(4, 6));
	    cal.set(Calendar.YEAR, annee);
	    cal.set(Calendar.MONTH, mois - 1);
	    SimpleDateFormat formatLibelle = new SimpleDateFormat("MMMMM yyyy", Locale.FRENCH);
	    String libelle = formatLibelle.format(cal.getTime());
	    libelle = libelle.substring(0, 1).toUpperCase() + libelle.substring(1);

	    // Alimenter le message de confirmation
	    AutorisationDroitsController.setMESSAGE_CONFIRMATION("L'affichage des droits pour le mois " + libelle + " a été autorisé.");
	    LOGGER.info("... ... Enregistrement de la validation d'affichage des droits pour tous les mois");
	}
	return showForm(request, response, errors, null);
    }

    public void setAutorisationDroitsManager(IAutorisationDroitsManager autorisationDroitsManager) {
	this.autorisationDroitsManager = autorisationDroitsManager;
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
