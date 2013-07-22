package i2.application.extranet.action;

import i2.application.aurige.bean.AutoEcole;
import i2.application.aurige.bean.Departement;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewAE;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.business.IPlanningManager;
import i2.application.extranet.business.authentification.IAutoEcoleManager;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.exceptions.DataValidateError;
import i2.application.extranet.business.exceptions.DataValidateWarning;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.IPlanningSemaineForm;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.Constants;
import i2.application.extranet.utils.WebBundle;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import csb.common.tools.date.DateFormater;

/**
 * 
 * @author Bull
 * 
 */
public abstract class AbstractPlanningController extends MultiActionController {

    private final static Logger logger = Logger.getLogger(AbstractPlanningController.class);

    protected WebBundle messages;

    private IPlanningManager planningManager;

    private String constantForJson;

    private Boolean modeAccessibilite;

    private String planningViewName;

    private IAutoEcoleManager autoEcoleManager;

    public static final String KEY_MODELE_LISTE_PLANNING = "lstLignesBeans";

    protected List<IViewLignePlanning> lstLignesBeans;

    protected AutoEcole getAutoEcoleConnected(HttpServletRequest request) {
	AutoEcole autoEcoleConnected = null;

	String aueCode = request.getParameter("aueCode");// envoyer lors de choix Etablissement
	String profil = getProfilUserConnected(request);
	if (aueCode != null && (profil.equals(EnumProfils.REPARTITEUR.getLibelle()) || profil.equals(EnumProfils.LECTEUR.getLibelle()))) {
	    // Traitement lors de l'action recherche Etbs
	    autoEcoleConnected = getAutoEcoleManager().readAutoEcole(aueCode);
	} else {
	    ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	    if (userConnected.getRestriction() instanceof AutoEcole) {
		autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	    }
	}
	return autoEcoleConnected;
    }

    protected String getMailUserConnected(HttpServletRequest request) {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	return userConnected.getEmail();
    }

    protected String getProfilUserConnected(HttpServletRequest request) {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	return userConnected.getLibelleProfil();
    }

    protected List<IViewLignePlanning> getListLignesBeans(AutoEcole autoEcole, HttpServletRequest request, Calendar cal) {
	String profilUserConnected = getProfilUserConnected(request);
	return planningManager.getPlanningPourCriteres(autoEcole, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, profilUserConnected);
    }

    protected ViewAnneeMois getViewAnneMoisSelected(HttpServletRequest request, List<ViewAnneeMois> listAnneeMois) {
	String moisSelection = request.getParameter("moisSelection"); // envoyer lors de clique sur le bouton Afficher
	String anneeMois = request.getParameter("anneeMois"); // envoyer lors de choix Etablissement

	ViewAnneeMois viewAnneeMoisSelection = ((listAnneeMois != null) && (listAnneeMois.size() > 0)) ? listAnneeMois.get(0) : null;

	String profil = getProfilUserConnected(request);
	if (anneeMois != null && (profil.equals(EnumProfils.REPARTITEUR.getLibelle()) || profil.equals(EnumProfils.LECTEUR.getLibelle()))) {
	    // Traitement lors de l'action recherche Etbs
	    moisSelection = anneeMois;
	}

	// Traitement lors de l'action afficher
	if (moisSelection != null) {
	    for (ViewAnneeMois item : listAnneeMois) {
		if (item.getId().equals(moisSelection)) {
		    viewAnneeMoisSelection = item;
		}
	    }
	}
	return viewAnneeMoisSelection;
    }

    protected Departement getDepartementConnected(HttpServletRequest request) {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	return userConnected.getDepartement();
    }

    /**
     * Abstraction de la méthode init
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	logger.info("Initialisation du planning pour un mois donné");
	String aueCode = request.getParameter("aueCode");// envoyer lors de choix Etablissement
	Map<String, Object> modele = new HashMap<String, Object>();
	List<ViewAnneeMois> listAnneeMois = planningManager.getListAnneeMois(getDepartementConnected(request), getProfilUserConnected(request));
	ViewAnneeMois viewAnneeMoisSelection = getViewAnneMoisSelected(request, listAnneeMois);

	// Init: Formatage des données
	Calendar cal = Calendar.getInstance();
	if (viewAnneeMoisSelection == null) {
	    // Quand le mois courant est egale ou inferieur au mois de lancement de l'appli
	    request.setAttribute("tableauNull", true);
	} else {
	    cal.setTime(DateFormater.formatIdDate(viewAnneeMoisSelection.getId()));
	    request.setAttribute("tableauNull", false);
	    /**
	     * si tableauVide = false alors on n'affiche un tableau rempli par les infos, Sinon, un tableau vide
	     */
	    boolean tableauVide = false;
	    lstLignesBeans = null;
	    String profil = getProfilUserConnected(request);
	    lstLignesBeans = getListLignesBeans(getAutoEcoleConnected(request), request, cal);
	    if (lstLignesBeans == null || lstLignesBeans.size() == 0) {
		tableauVide = true;
		request.setAttribute("mois", viewAnneeMoisSelection.getLibelle());
	    }
	    request.setAttribute("tableauVide", tableauVide);
	    boolean editable = planningManager.isEditable(getDepartementConnected(request), viewAnneeMoisSelection.getId(), getProfilUserConnected(request));
	    request.setAttribute("isEditable", editable);

	    // Traitement : Récupérer la liste des écoles de conduite pour un département et pour un mois donné
	    List<ViewAE> lstAue = null;
	    if (profil.equals(EnumProfils.REPARTITEUR.getLibelle()) || profil.equals(EnumProfils.LECTEUR.getLibelle()))
		lstAue = planningManager.getListAEToPlanning(viewAnneeMoisSelection.getId(), getDepartementConnected(request));
	    // Final : Convertir les données et les envoyer vers le client en Format JSON
	    if (!this.getModeAccessibilite()) {
		// L'accés n'est pas réalisé en mode texte le controleur abstrait
		// renvoie un objet JSON dans le modèle
		modele.put(getConstantForJson(), ActionFactory.createAffichageDuPlanning(lstLignesBeans, cal, editable, tableauVide));
		modele.put(getConstantForJson(), ActionFactory.addListAutoEcolesByMois((JSONObject) modele.get(getConstantForJson()), lstAue));
		if (aueCode != null && getAutoEcoleConnected(request) != null && (profil.equals(EnumProfils.REPARTITEUR.getLibelle()) || profil.equals(EnumProfils.LECTEUR.getLibelle())))
		    modele.put(getConstantForJson(), ActionFactory.addUserSelected((JSONObject) modele.get(getConstantForJson()), getAutoEcoleConnected(request).getAueCodeRafael(),
			    getAutoEcoleConnected(request).getAueCode()));
		modele.put(KEY_MODELE_LISTE_PLANNING, lstLignesBeans);

		String permisSelection = request.getParameter("permisSelection");
		String centreSelection = request.getParameter("centreSelection");

		if (permisSelection == null) {
		    permisSelection = "(Tous)";
		}
		if (centreSelection == null) {
		    centreSelection = "(Tous)";
		}

		modele.put(getConstantForJson(), ActionFactory.addListMois((JSONObject) modele.get(getConstantForJson()), listAnneeMois, viewAnneeMoisSelection));
		modele.put(getConstantForJson(), ActionFactory.addCentreEtPermisSelection((JSONObject) modele.get(getConstantForJson()), permisSelection, centreSelection));

	    } else {
		// Pour l'accesibilité d'autre données peuvent être ajoutée au model
		// ici
		modele.put(KEY_MODELE_LISTE_PLANNING, lstLignesBeans);
	    request.getSession().setAttribute(Constants.PLANNING_TXT, lstLignesBeans);
		// .... à définir lors de dev
	    }
	}

	return new ModelAndView(this.planningViewName, Constants.MODELE, modele);
    }

    /**
     * Méthode d'abstraction de sauvegarde des données modifiées.
     * 
     * @param request
     * @param response
     * @param command
     * @return
     * @throws Exception
     * @throws ApplicationException
     */

    public ModelAndView abstractSubmitModification(HttpServletRequest request, HttpServletResponse response, Object command) throws ApplicationException {
	List<IViewLignePlanning> lstPlanningSession = (List<IViewLignePlanning>) request.getSession().getAttribute(Constants.SES_PLANNING);

	try {
	    List<IViewLignePlanning> lstLigneModif = ((IPlanningSemaineForm) command).getLignePlanning();
	    AutoEcole ecole = getAutoEcoleConnected(request);
	    if (ecole != null) {

		planningManager.saveModifPlanning(lstLigneModif, lstPlanningSession, ecole, getMailUserConnected(request), getProfilUserConnected(request));
	    }
	    logger.debug("DataValidate success");
	} catch (DataValidateError e) {

	    logger.debug(e);
	    throw e;
	} catch (DataValidateWarning e) {

	    logger.debug(e);
	    throw e;
	} catch (ApplicationException e) {
	    logger.error("ApplicationException catched:" + e.getMessage(), e);
	    throw e;
	} finally {
	    request.getSession().setAttribute(Constants.SES_PLANNING, lstPlanningSession);

	}

	return null;
    }

    /**
     * @return the planningManager
     */
    public IPlanningManager getPlanningManager() {
	return planningManager;
    }

    /**
     * @param planningManager
     *            the planningManager to set
     */
    public void setPlanningManager(IPlanningManager planningManager) {
	this.planningManager = planningManager;
    }

    /**
     * @return the constantForJson
     */
    public String getConstantForJson() {
	return constantForJson;
    }

    /**
     * @param constantForJson
     *            the constantForJson to set
     */
    public void setConstantForJson(String constantForJson) {
	this.constantForJson = constantForJson;
    }

    /**
     * @return the planningViewName
     */
    public String getPlanningViewName() {
	return planningViewName;
    }

    /**
     * @param planningViewName
     *            the planningViewName to set
     */
    public void setPlanningViewName(String planningViewName) {
	this.planningViewName = planningViewName;
    }

    /**
     * @return the modeAccessibilite
     */
    public Boolean getModeAccessibilite() {
	return modeAccessibilite;
    }

    /**
     * @param modeAccessibilite
     *            the modeAccessibilite to set
     */
    public void setModeAccessibilite(Boolean modeAccessibilite) {
	this.modeAccessibilite = modeAccessibilite;
    }

    public WebBundle getMessages() {
	return messages;
    }

    public void setMessages(WebBundle messages) {
	this.messages = messages;
    }

    public IAutoEcoleManager getAutoEcoleManager() {
	return autoEcoleManager;
    }

    public void setAutoEcoleManager(IAutoEcoleManager autoEcoleManager) {
	this.autoEcoleManager = autoEcoleManager;
    }

}
