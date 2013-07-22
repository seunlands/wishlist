package i2.application.extranet.action;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.txt.CriteresRechercheTxtView;
import i2.application.extranet.bean.view.txt.RechercheTxt;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.filtres.IFiltreManager;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import i2.application.extranet.form.txt.SubmitFieldsTxtForm;
import i2.application.extranet.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Bull
 * 
 */
public abstract class AbstractPlanningTxtController extends AbstractPlanningController {

    private final static Logger logger = Logger.getLogger(AbstractPlanningTxtController.class);

    private IFiltreManager filtreManager;

    public IFiltreManager getFiltreManager() {
	return filtreManager;
    }

    public void setFiltreManager(IFiltreManager filtreManager) {
	this.filtreManager = filtreManager;
    }

    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {

	RechercheTxtCompositeForm command = new RechercheTxtCompositeForm();
	command.setRechercheForm(new RechercheTxt());
	command.setSubmitFieldsTxtForm(new SubmitFieldsTxtForm());

	List<ViewAnneeMois> listAnneeMois = getPlanningManager().getListAnneeMois(getAutoEcoleConnected(request).getDepartement(), getProfilUserConnected(request));
	CriteresRechercheTxtView criteres = new CriteresRechercheTxtView();
	criteres.setListAnneeMois(listAnneeMois);
	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);
	request.getSession().removeAttribute(Constants.RESULTATS_MODEL_TXT);
	request.getSession().removeAttribute(Constants.SEANCE_MODEL_TXT);

	if (criteres.getListAnneeMois().size() == 1) {
	    command.getRechercheForm().setAnneeMoisSelection(listAnneeMois.iterator().next().getId());
	    return rechercheDate(request, response, command);
	}

	return createModelAndView(command, criteres);

    }

    public ModelAndView redirige(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) throws ApplicationException {

	if (request.getParameter(Constants.ACTION_POST_PARAM) != null) {
	    // A voir si utile : && !request.getParameter("submit").equals("Annuler")) {
	    String methode = request.getParameter(Constants.ACTION_POST_PARAM);
	    if (methode.equals(Constants.ACTION_ENREGISTRE)) {
		return submitModification(request, response, command);
	    } else {

		// pour la recherche, on resette les parties du form existantes
		if (command.getAffectationForm() != null) {
		    command.setAffectationForm(null);
		}
		if (command.getDetailForm() != null) {
		    command.setDetailForm(null);
		}
		request.getSession().removeAttribute(Constants.RESULTATS_MODEL_TXT);
		request.getSession().removeAttribute(Constants.SEANCE_MODEL_TXT);

		if (methode.equals(Constants.ACTION_RECHERCHE_DATE))
		    return rechercheDate(request, response, command);
		else if (methode.equals(Constants.ACTION_AFFICHE))
		    return afficher(request, response, command);
	    }
	}

	return null;

    }

    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, Object command1) {
	return null;
    }

    protected ModelAndView rechercheDate(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {

	SimpleDateFormat formatId = new SimpleDateFormat("yyyyMM");
	Calendar cal = Calendar.getInstance();
	try {
	    cal.setTime(formatId.parse(command.getRechercheForm().getAnneeMoisSelection()));
	} catch (ParseException e) {
	    logger.warn("Annee mois format error : annemois = " + command.getRechercheForm().getAnneeMoisSelection());
	}

	AutoEcole ae = getAutoEcoleConnected(request);

	List<IViewLignePlanning> lstLignesBeans = getListLignesBeans(ae, request, cal);
	if (lstLignesBeans == null) {
	    lstLignesBeans = new ArrayList<>();
	}
	request.getSession().setAttribute(Constants.PLANNING_TXT, lstLignesBeans);

	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	criteres.setListPermis(getFiltreManager().getFiltrePermis(lstLignesBeans));
	criteres.setListCentre(getFiltreManager().getFiltreCentres(lstLignesBeans));
	criteres.getListSalle().clear();
	criteres.getListJour().clear();
	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);

	if (criteres.getListPermis().size() == 1 && criteres.getListCentre().size() == 1) {
	    command.getRechercheForm().setCexNumero(criteres.getListCentre().iterator().next().getId());
	    command.getRechercheForm().setPermisId(criteres.getListPermis().iterator().next().getId());
	    return rechercheCentreEtPermis(request, response, command);
	}

	return createModelAndView(command, criteres);

    }

    protected abstract ModelAndView rechercheCentreEtPermis(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command);

    protected abstract ModelAndView afficher(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command);

    // Méthodes de creation du modele

    protected ModelAndView createModelAndView(Object command, CriteresRechercheTxtView criteres) {

	ModelAndView mv = createModelAndView(command);
	mv.addObject(Constants.CRITERES_MODEL_TXT, criteres);
	return mv;

    }

    protected ModelAndView createModelAndView(Object command) {

	ModelAndView mv = new ModelAndView(getPlanningViewName());
	mv.addObject(Constants.COMMAND_TXT, command);
	return mv;

    }

}
