package i2.application.extranet.action;

import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewCentreExamen;
import i2.application.extranet.bean.view.ViewLibelle;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.planningjour.AbstractSeance;
import i2.application.extranet.bean.view.txt.CriteresRechercheTxtView;
import i2.application.extranet.bean.view.txt.PlanningJourTxtView;
import i2.application.extranet.bean.view.txt.RechercheTxt;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import i2.application.extranet.utils.Constants;

import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.date.MonthForCalendar;

public abstract class AbstractPlanningJourTxtController extends AbstractPlanningTxtController {

    private final static Logger logger = Logger.getLogger(AbstractPlanningJourTxtController.class);

    protected abstract Object getRecapitulatif(HttpServletRequest request, int annee, int mois, List<IViewLignePlanning> lstPlanning);

    @Override
    public ModelAndView redirige(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) throws ApplicationException {

	ModelAndView mv = super.redirige(request, response, command);

	if (mv == null && request.getParameter(Constants.ACTION_POST_PARAM) != null) {
	    String methode = request.getParameter(Constants.ACTION_POST_PARAM);

	    if (methode.equals(Constants.ACTION_RECHERCHE_CENTRE))
		mv = rechercheCentreEtPermis(request, response, command);
	    if (methode.equals(Constants.ACTION_RECHERCHE_SALLE))
		mv = rechercheSalle(request, response, command);
	}

	return mv;
    }

    @Override
    protected ModelAndView rechercheCentreEtPermis(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {

	@SuppressWarnings("unchecked")
	List<IViewLignePlanning> lstLignesBeans = (List<IViewLignePlanning>) request.getSession().getAttribute(Constants.PLANNING_TXT);
	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	{
	    criteres.setListPermis(getFiltreManager().getFiltrePermis(lstLignesBeans));
	    criteres.setListCentre(getFiltreManager().getFiltreCentres(lstLignesBeans));
	    criteres.setListSalle(getFiltreManager().getFiltreRessources(lstLignesBeans, command.getRechercheForm()));
	    criteres.getListJour().clear();
	}
	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);

	if (criteres.getListSalle().size() == 1) {
	    command.getRechercheForm().setSalleId(criteres.getListSalle().iterator().next().getId());
	    return rechercheSalle(request, response, command);
	}

	return createModelAndView(command, criteres);

    }

    protected ModelAndView rechercheSalle(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {

	@SuppressWarnings("unchecked")
	List<IViewLignePlanning> lstLignesBeans = (List<IViewLignePlanning>) request.getSession().getAttribute(Constants.PLANNING_TXT);
	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	{
	    criteres.setListPermis(getFiltreManager().getFiltrePermis(lstLignesBeans));
	    criteres.setListCentre(getFiltreManager().getFiltreCentres(lstLignesBeans));
	    criteres.setListSalle(getFiltreManager().getFiltreRessources(lstLignesBeans, command.getRechercheForm()));
	    criteres.setListJour(getFiltreManager().getFiltreJour(lstLignesBeans, command.getRechercheForm()));
	}
	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);

	if (criteres.getListJour().size() == 1) {
	    command.getRechercheForm().setJour(criteres.getListJour().iterator().next().getId());
	    return afficher(request, response, command);
	}

	return createModelAndView(command, criteres);

    }

    // Méthodes de creation du modele

    @Override
    protected ModelAndView afficher(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {

	// enregistre les criteres

	RechercheTxt criteresSelection = command.getRechercheForm();
	request.getSession().setAttribute(Constants.CRITERES_FORM_TXT, criteresSelection);

	@SuppressWarnings("unchecked")
	List<IViewLignePlanning> lstLignesBeans = (List<IViewLignePlanning>) request.getSession().getAttribute(Constants.PLANNING_TXT);
	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	{
	    criteres.setListPermis(getFiltreManager().getFiltrePermis(lstLignesBeans));
	    criteres.setListCentre(getFiltreManager().getFiltreCentres(lstLignesBeans));
	    criteres.setListSalle(getFiltreManager().getFiltreRessources(lstLignesBeans, command.getRechercheForm()));
	    criteres.setListJour(getFiltreManager().getFiltreJour(lstLignesBeans, command.getRechercheForm()));
	}
	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);

	// prépare le résultat

	PlanningJourTxtView resultat = new PlanningJourTxtView();
	// SimpleDateFormat formatId = new SimpleDateFormat("yyyyMM");
	// Calendar cal = Calendar.getInstance();
	MonthForCalendar anneeMois = null;
	if (command.getRechercheForm().getAnneeMoisSelection() != null) {
	    anneeMois = new MonthForCalendar(command.getRechercheForm().getAnneeMoisSelection());
	} else {
	    anneeMois = new MonthForCalendar(GregorianCalendar.getInstance());
	    logger.warn("Annee mois format error : annemois = " + command.getRechercheForm().getAnneeMoisSelection());
	}
	String anneeMoisSelection = anneeMois.getLibelleMois();
	resultat.setAnneeMois(anneeMoisSelection);
	for (ViewCentreExamen v : criteres.getListCentre()) {
	    if (command.getRechercheForm().getCexNumero().equals(v.getId())) {
		resultat.setCentre(v.getLibelle());
		break;
	    }
	}
	for (ViewPermis v : criteres.getListPermis()) {
	    if (command.getRechercheForm().getPermisId().equals(v.getId())) {
		resultat.setPermis(v.getLibelle());
		break;
	    }
	}
	for (ViewLibelle v : criteres.getListSalle()) {
	    if (command.getRechercheForm().getSalleId().equals(v.getId())) {
		resultat.setRessource(v.getLibelle());
		break;
	    }
	}
	resultat.setJour(command.getRechercheForm().getJour());
	List<AbstractSeance> lstSeances = getFiltreManager().getFiltrePlanningJour(lstLignesBeans, command.getRechercheForm());
	resultat.setSeances(lstSeances);
	Object lstRecap = getRecapitulatif(request, anneeMois.getAnnee(), anneeMois.getMois(), lstLignesBeans);
	resultat.setRecapitulatif(lstRecap);
	request.getSession().setAttribute(Constants.RESULTATS_MODEL_TXT, resultat);

	return createModelAndView(command, criteres, criteresSelection, resultat);

    }

    public ModelAndView handleBindException(HttpServletRequest request, HttpServletResponse response, ServletRequestBindingException bindingException) {

	logger.warn("binding problem : " + bindingException);
	BindException bindException = (BindException) bindingException.getRootCause();

	PlanningJourTxtView resultat = (PlanningJourTxtView) request.getSession().getAttribute(Constants.RESULTATS_MODEL_TXT);
	if (resultat == null) {
	    String m = "planningJour null en session";
	    logger.warn(m);
	    throw new IllegalStateException(m);
	}

	bindException.reject("form.incorrect.data");

	ModelAndView mv = new ModelAndView(getPlanningViewName());
	{
	    mv.addAllObjects(bindException.getModel());
	    CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	    mv.addObject(Constants.CRITERES_MODEL_TXT, criteres);
	    RechercheTxtCompositeForm command1 = (RechercheTxtCompositeForm) bindException.getModel().get("command");
	    RechercheTxt criteresSelection = (RechercheTxt) request.getSession().getAttribute(Constants.CRITERES_FORM_TXT);
	    command1.setRechercheForm(criteresSelection);
	    mv.addObject(Constants.RESULTATS_MODEL_TXT, resultat);
	}

	return mv;

    }

    /**
     * Met à jour le modèle en session après une modification (Refresh de la page, récapitulatif et résultat).
     * 
     * @param request
     * @param command
     */
    protected void updateModel(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {

	// mise à jour du reste du modèle...
	RechercheTxt rechercheForm = (RechercheTxt) request.getSession().getAttribute(Constants.CRITERES_FORM_TXT);
	command.setRechercheForm(rechercheForm);
	// ... les lignes du tableau (attention on évite reset des critères)
	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	rechercheDate(request, response, command);
	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);
	// ... le récapitulatif
	afficher(request, response, command);

    }

    // Méthodes de creation du modele

    protected ModelAndView createModelAndView(Object command, CriteresRechercheTxtView criteres, RechercheTxt criteresSelection, PlanningJourTxtView resultats) {

	ModelAndView mv = createModelAndView(command, criteres);
	((RechercheTxtCompositeForm) command).setRechercheForm(criteresSelection);
	mv.addObject(Constants.RESULTATS_MODEL_TXT, resultats);
	return mv;

    }

    protected ModelAndView createModelAndView(Object command, HttpServletRequest request, PlanningJourTxtView resultats) {

	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	RechercheTxt criteresSelection = (RechercheTxt) request.getSession().getAttribute(Constants.CRITERES_FORM_TXT);
	return createModelAndView(command, criteres, criteresSelection, resultats);

    }

}
