package i2.application.extranet.action.affectation;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.AbstractPlanningJourTxtController;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.affectation.AffectationCreneauHoraire;
import i2.application.extranet.bean.view.affectation.AffectationSeanceDetail;
import i2.application.extranet.bean.view.affectation.ViewRecapitulatifHebdomadaire;
import i2.application.extranet.bean.view.planningjour.AbstractSeance;
import i2.application.extranet.bean.view.txt.PlanningJourTxtView;
import i2.application.extranet.bean.view.txt.ResultatTxtView;
import i2.application.extranet.business.affectation.IAffectationManager;
import i2.application.extranet.business.exceptions.DataValidateError;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.affetation.AffectationCreneauForm;
import i2.application.extranet.form.affetation.AffectationDetailForm;
import i2.application.extranet.form.affetation.AffectationForm;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import i2.application.extranet.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author BULL SAS
 * 
 *         Contrôleur d'actions pour le tableau des unités affectées.
 */

public class AffectationTxtController extends AbstractPlanningJourTxtController {

    private final static Logger logger = Logger.getLogger(AffectationTxtController.class);

    protected IAffectationManager affectationManager;

    protected AffectationHelper affectationHelper;

    @Override
    protected Object getRecapitulatif(HttpServletRequest request, int annee, int mois, List<IViewLignePlanning> lstPlanning) {
	List<ViewRecapitulatifHebdomadaire> lstRecap = affectationManager.getListRecapitulatifPourCriteres(getAutoEcoleConnected(request), annee, mois);
	return lstRecap;
    }

    public ModelAndView getDetail(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command1) {

	AffectationDetailForm command = command1.getDetailForm();

	if (logger.isDebugEnabled())
	    logger.debug("AffectationDetailForm=" + command);

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole) {

	    AutoEcole aue = (AutoEcole) userConnected.getRestriction();
	    List<Integer> ids = new ArrayList<>();
	    if (command != null) {
		ids.addAll(command.getIds());
	    }
	    AffectationSeanceDetail data = affectationManager.getAffectationsByActiviteAndCreneau(aue, ids, userConnected.getLibelleProfil());
	    getAffectationHelper().sortSeances(data);

	    request.getSession().setAttribute(Constants.SEANCE_MODEL_TXT, data);

	    return createModelAndView(command1, request, data);

	} else {
	    return sendUnauthorized("getDetail", userConnected, request, response, command1);
	}

    }

    public ModelAndView submit(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command1) {

	AffectationForm command = command1.getAffectationForm();

	if (logger.isDebugEnabled()) {
	    logger.debug("#submit : form = " + command);
	}

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	ResultatTxtView resultat = new ResultatTxtView();

	// filtrer les profils admin et lecteur
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {

	    boolean fail = false;
	    AutoEcole aue = (AutoEcole) userConnected.getRestriction();

	    // sélectionne les champs modifiés
	    List<AffectationCreneauHoraire> affectationList = new ArrayList<>();
	    AffectationSeanceDetail seance = (AffectationSeanceDetail) request.getSession().getAttribute(Constants.SEANCE_MODEL_TXT);
	    seance = affectationHelper.cloneAffectationSeanceDetail(seance);
	    if (seance != null) {

		Map<AffectationCreneauHoraireIndex, AffectationCreneauHoraire> existing = new HashMap<>();
		for (AffectationCreneauHoraire ach : seance.getCreneauxHoraire()) {
		    existing.put(new AffectationCreneauHoraireIndex(ach.getActId(), ach.getIndex()), ach);
		}

		for (AffectationCreneauForm affectationCreneauForm : command.getAffectations()) {
		    AffectationCreneauHoraire existingAch = existing.get(new AffectationCreneauHoraireIndex(affectationCreneauForm.getActId(), affectationCreneauForm.getIndex()));
		    if (existingAch != null && existingAch.getNbPlaceResa() != affectationCreneauForm.getNbUnites()) {
			AffectationCreneauHoraire affectation = getAffectationHelper().convertAffectationForm(affectationCreneauForm);
			if (affectation != null) {
			    affectationList.add(affectation);
			}
		    }
		}

		if (!affectationList.isEmpty()) {

		    if (logger.isTraceEnabled())
			for (AffectationCreneauHoraire ach : affectationList) {
			    logger.trace("Creneau modifie : " + ach);
			}

		    List<AbstractSeance> lstSeance = ((PlanningJourTxtView) request.getSession().getAttribute("resultats")).getSeances();
		    Map<Integer, Long> mapActTime = new HashMap<Integer, Long>();
		    for (AbstractSeance s : lstSeance) {
			for (int i = 0; i < s.getActiviteIdList().size(); i++) {
			    Integer actId = s.getActiviteIdList().get(i);
			    mapActTime.put(actId, s.getTimestampList().get(i));
			}
		    }

		    try {
			affectationManager.saveAffectation(aue, affectationList, userConnected.getEmail(), mapActTime);
		    } catch (DataValidateError e) {
			logger.debug(e);
			for (String m : e.getListMessage()) {
			    resultat.getErrorMessages().add(m);
			}
			fail = true;
		    } catch (TechnicalError e) {
			logger.warn(e);
			for (String m : e.getListMessage()) {
			    resultat.getErrorMessages().add(m);
			}
			fail = true;
		    }

		    // mise à jour du modèle
		    for (AffectationCreneauHoraire ach : affectationList) {
			AffectationCreneauHoraire existingAch = existing.get(new AffectationCreneauHoraireIndex(ach.getActId(), ach.getIndex()));
			if (existingAch != null) {
			    existingAch.setNbPlaceResa(ach.getNbPlaceResa());
			}
		    }

		    if (!fail) {
			// sauvegarde du modèle
			request.getSession().setAttribute(Constants.SEANCE_MODEL_TXT, seance);
			resultat.getSuccessMessages().add(messages.getMessage("msg.common.modifs.enregistrees"));
		    }

		} else {
		    logger.debug("pas de modification");
		}

		updateModel(request, response, command1);

		ModelAndView mv = createModelAndView(command1, request, seance);
		mv.addObject(Constants.MESSAGES_MODEL_TXT, resultat);
		return mv;

	    } else {
		String m = "seance null en session";
		logger.warn(m);
		throw new IllegalStateException(m);
	    }
	} else {
	    // non autorisé
	    return sendUnauthorized("submit", userConnected, request, response, command1);
	}

    }

    protected ModelAndView sendUnauthorized(final String action, final ViewUtilisateur userConnected, final HttpServletRequest request, final HttpServletResponse response,
	    final RechercheTxtCompositeForm command) {

	ModelAndView mv = createModelAndView(command, request);
	ResultatTxtView resultat = new ResultatTxtView();
	resultat.getErrorMessages().add(messages.getMessage("action.non.autorisee"));
	mv.addObject(Constants.MESSAGES_MODEL_TXT, resultat);
	logger.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	return mv;

    }

    // Méthodes de creation du modele

    protected ModelAndView createModelAndView(Object command, HttpServletRequest request, AffectationSeanceDetail seance) {

	PlanningJourTxtView resultat = (PlanningJourTxtView) request.getSession().getAttribute(Constants.RESULTATS_MODEL_TXT);
	ModelAndView mv = createModelAndView(command, request, resultat);
	mv.addObject(Constants.SEANCE_MODEL_TXT, seance);
	return mv;

    }

    protected ModelAndView createModelAndView(Object command, HttpServletRequest request) {

	AffectationSeanceDetail seance = (AffectationSeanceDetail) request.getSession().getAttribute(Constants.SEANCE_MODEL_TXT);
	ModelAndView mv = createModelAndView(command, request, seance);
	return mv;

    }

    public AffectationHelper getAffectationHelper() {
	return affectationHelper;
    }

    public void setAffectationHelper(AffectationHelper affectationHelper) {
	this.affectationHelper = affectationHelper;
    }

    public void setAffectationManager(IAffectationManager affectationManager) {
	this.affectationManager = affectationManager;
    }

}
