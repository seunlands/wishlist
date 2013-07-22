package i2.application.extranet.action;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewCentreExamen;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.attribution.LigneAttributionBean;
import i2.application.extranet.bean.view.reservation.LigneReservationBean;
import i2.application.extranet.bean.view.txt.CriteresRechercheTxtView;
import i2.application.extranet.bean.view.txt.PlanningSemTxtView;
import i2.application.extranet.bean.view.txt.RechercheTxt;
import i2.application.extranet.bean.view.txt.ResultatTxtView;
import i2.application.extranet.business.IAbstractPlanningSem;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.exceptions.DataValidateError;
import i2.application.extranet.business.exceptions.DataValidateWarning;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import i2.application.extranet.utils.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.date.DateFormater;
import csb.common.tools.date.MonthForCalendar;

/**
 * @author Bull-SAS
 * 
 */
public abstract class PlanningSemTxtController extends AbstractPlanningTxtController {
    private final static Logger LOGGER = Logger.getLogger(PlanningSemTxtController.class);

    protected IAbstractPlanningSem abstractPlanningSem;

    /**
     * Injection abstractPlanningSem
     * 
     * @param abstractPlanningSem
     */
    public void setAbstractPlanningSem(IAbstractPlanningSem abstractPlanningSem) {
	this.abstractPlanningSem = abstractPlanningSem;
    }

    @Override
    protected ModelAndView afficher(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {
	LOGGER.debug("#PlanningSemController.afficher");

	RechercheTxt criteresSelection = command.getRechercheForm();
	request.getSession().setAttribute(Constants.CRITERES_FORM_TXT, criteresSelection);

	// Préparer le format à afficher : Juillet 2012
	MonthForCalendar anneeMois = null;
	if (command.getRechercheForm().getAnneeMoisSelection() != null) {
	    anneeMois = new MonthForCalendar(command.getRechercheForm().getAnneeMoisSelection());
	} else {
	    anneeMois = new MonthForCalendar(GregorianCalendar.getInstance());
	    LOGGER.warn("Annee mois format error : annemois = " + command.getRechercheForm().getAnneeMoisSelection());
	}

	String anneeMoisSelection = anneeMois.getLibelleMois();

	// Récupération de la liste des semaines
	List<String> lstHeaders = DateFormater.getSemaineHeader(anneeMois.getFirstDayOfMonth());

	// Récuperer les critères de recherche
	List<IViewLignePlanning> lstLignesBeans = (List<IViewLignePlanning>) request.getSession().getAttribute(Constants.PLANNING_TXT);
	i2.application.extranet.bean.view.txt.CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	criteres.setListPermis(getFiltreManager().getFiltrePermis(lstLignesBeans));
	criteres.setListCentre(getFiltreManager().getFiltreCentres(lstLignesBeans));

	// Préparer les informations du model à transmettre
	PlanningSemTxtView resultat = new PlanningSemTxtView();
	resultat.setAnneeMois(anneeMoisSelection);
	resultat.setFormatAnneeMois(command.getRechercheForm().getAnneeMoisSelection());
	resultat.setCalendarAnneeMois(anneeMois.getFirstDayOfMonth());

	for (ViewCentreExamen v : criteres.getListCentre()) {
	    if (command.getRechercheForm().getCexNumero().equals(v.getId())) {
		resultat.setCentre(v.getLibelle());
		resultat.setCexNumero(v.getId());
		break;
	    }

	}
	for (ViewPermis v : criteres.getListPermis()) {
	    if (command.getRechercheForm().getPermisId().equals(v.getId())) {
		resultat.setPermis(v.getLibelle());
		break;
	    }
	}

	// Récupérer le mode d'accès au panneau
	boolean editable = this.getPlanningManager().isEditable(getAutoEcoleConnected(request).getDepartement(), command.getRechercheForm().getAnneeMoisSelection(), getProfilUserConnected(request));

	// Récupérer de la ligne planning en filtrant lstLignesBeans selon les critères de recherches
	List<IViewLignePlanning> lstLignes = this.getFiltreManager().getFiltrePlanningSemaine(lstLignesBeans, command.getRechercheForm());

	if (lstLignes == null) {
	    lstLignes = new ArrayList<>();
	}

	if (!lstLignes.isEmpty()) {
	    if (lstLignes.get(0) instanceof LigneAttributionBean) {
		command.getSubmitFieldsTxtForm().setAttribution((LigneAttributionBean) lstLignes.get(0));
		resultat.setLignesPlanning((LigneAttributionBean) lstLignes.get(0));
	    }
	    if (lstLignes.get(0) instanceof LigneReservationBean) {
		command.getSubmitFieldsTxtForm().setReservation((LigneReservationBean) lstLignes.get(0));
		resultat.setLignesPlanning((LigneReservationBean) lstLignes.get(0));
	    }
	}

	resultat = this.updateRecap(resultat, lstLignesBeans, getProfilUserConnected(request), getAutoEcoleConnected(request));

	// Alimenter le ModelAndView par les informations nécessaire
	ModelAndView mv = createModelAndView(command, request, resultat);
	if (lstLignes.isEmpty()) {
	    mv.addObject("tableauVide", true);
	    request.getSession().setAttribute("tableauVide", true);
	}

	// Récupérer la date de cloture de la phase en question (Reservation ou Attribution)
	Calendar dateCloture = abstractPlanningSem.getDateLimite(getAutoEcoleConnected(request).getDepartement(), command.getRechercheForm().getAnneeMoisSelection(), getProfilUserConnected(request));
	if (dateCloture != null) {
	    // Format de la date comme : Mercredi 11 Avril 2012
	    Locale locale = Locale.getDefault();
	    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
	    request.getSession().setAttribute("dateCloture", dateFormat.format(dateCloture.getTime()));
	    mv.addObject("dateCloture", dateFormat.format(dateCloture.getTime()));
	}

	// Mettre en session les objets nécessaire
	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);
	request.getSession().setAttribute(Constants.CRITERES_FORM_TXT, command.getRechercheForm());
	request.getSession().setAttribute(Constants.RESULTATS_MODEL_TXT, resultat);
	request.getSession().setAttribute("lstHeaders", lstHeaders);
	request.getSession().setAttribute("editable", editable);

	// Alimenter le ModelAndView
	mv.addObject("lstHeaders", lstHeaders);
	mv.addObject("editable", editable);
	mv.addObject("permisSelected", command.getRechercheForm().getPermisId());
	mv.addObject("centreSelected", command.getRechercheForm().getCexNumero());

	return mv;
    }

    @Override
    protected ModelAndView rechercheCentreEtPermis(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {
	return afficher(request, response, command);
    }

    /**
     * 
     * @param lignePlanning
     * @param ecole
     * @param mailUser
     * @param profilUserConnected
     */
    protected abstract void saveAbstract(List<IViewLignePlanning> lignePlanning, List<IViewLignePlanning> lstPlanningSession, AutoEcole ecole, String mailUser, String profilUserConnected,
	    PlanningSemTxtView resultat) throws ApplicationException;

    protected abstract PlanningSemTxtView updateRecap(PlanningSemTxtView resultat, List<IViewLignePlanning> lstLignePlanningSession, String userProfilConnected, AutoEcole ecole);

    // Méthodes de creation du modele

    public ModelAndView submit(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {

	// Récuperer les critères de recherche
	List<IViewLignePlanning> lstLignesBeans = (List<IViewLignePlanning>) request.getSession().getAttribute(Constants.PLANNING_TXT);

	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	criteres.setListPermis(getFiltreManager().getFiltrePermis(lstLignesBeans));
	criteres.setListCentre(getFiltreManager().getFiltreCentres(lstLignesBeans));

	PlanningSemTxtView resultat = (PlanningSemTxtView) request.getSession().getAttribute(Constants.RESULTATS_MODEL_TXT);

	if (resultat == null) {
	    throw new IllegalStateException("planning semaine null en session");
	}

	Calendar anneeMois = resultat.getCalendarAnneeMois();

	// filtrer les profils admin et lecteur
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	ResultatTxtView resultatMsgTxt = new ResultatTxtView();

	// Enregistrement des données
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {
	    boolean fail = false;
	    try {
		if (!lstLignesBeans.isEmpty()) {
		    List<IViewLignePlanning> lstLigneModif = new ArrayList<>();
		    if (lstLignesBeans.get(0) instanceof LigneReservationBean) {
			LigneReservationBean ligneResaBean = command.getSubmitFieldsTxtForm().getReservation();
			ligneResaBean.setCexNumero(resultat.getCexNumero());
			ligneResaBean.setPermis(resultat.getPermis());
			ligneResaBean.setMoisAnnee(resultat.getFormatAnneeMois());
			lstLigneModif.add(ligneResaBean);
		    } else if (lstLignesBeans.get(0) instanceof LigneAttributionBean) {
			LigneAttributionBean ligneAttribuBean = command.getSubmitFieldsTxtForm().getAttribution();
			ligneAttribuBean.setCexNumero(resultat.getCexNumero());
			ligneAttribuBean.setPermis(resultat.getPermis());
			ligneAttribuBean.setMoisAnnee(resultat.getFormatAnneeMois());
			lstLigneModif.add(ligneAttribuBean);
		    }
		    this.saveAbstract(lstLigneModif, lstLignesBeans, getAutoEcoleConnected(request), getMailUserConnected(request), getProfilUserConnected(request), resultat);
		}
	    } catch (DataValidateError e) {
		LOGGER.debug(e);
		fail = true;
		for (String m : e.getListMessage()) {
		    resultatMsgTxt.getErrorMessages().add(m);
		}
	    } catch (DataValidateWarning e) {
		LOGGER.debug(e);
		fail = false;
		for (String m : e.getListMessage()) {
		    resultatMsgTxt.getWarnMessages().add(m);
		}
	    } catch (ApplicationException e) {
		LOGGER.debug(e);
	    } finally {
		// remettre le nouveau planning en session
		AutoEcole ae = getAutoEcoleConnected(request);
		lstLignesBeans = getListLignesBeans(ae, request, anneeMois);
		request.getSession().setAttribute(Constants.PLANNING_TXT, lstLignesBeans);
	    }
	    if (!fail) {
		resultatMsgTxt.getSuccessMessages().add(messages.getMessage("msg.common.modifs.enregistrees"));
	    }
	}

	// Alimenter la commande par les informations nécessaire
	command.getRechercheForm().setAnneeMoisSelection(resultat.getFormatAnneeMois());
	command.getRechercheForm().setCexNumero(resultat.getCexNumero());
	command.getRechercheForm().setPermisId(resultat.getPermis());

	// Récupération de la ligne planning après modification
	List<IViewLignePlanning> lstFilter = this.getFiltreManager().getFiltrePlanningSemaine(lstLignesBeans, command.getRechercheForm());

	if (lstFilter == null) {
	    lstFilter = new ArrayList<>();
	}

	if (!lstFilter.isEmpty()) {
	    if (lstFilter.get(0) instanceof LigneAttributionBean) {
		command.getSubmitFieldsTxtForm().setAttribution((LigneAttributionBean) lstFilter.get(0));
		resultat.setLignesPlanning((LigneAttributionBean) lstFilter.get(0));
	    }
	    if (lstFilter.get(0) instanceof LigneReservationBean) {
		command.getSubmitFieldsTxtForm().setReservation((LigneReservationBean) lstFilter.get(0));
		resultat.setLignesPlanning((LigneReservationBean) lstFilter.get(0));
	    }
	}

	request.getSession().setAttribute(Constants.CRITERES_MODEL_TXT, criteres);

	resultat = this.updateRecap(resultat, lstLignesBeans, getProfilUserConnected(request), getAutoEcoleConnected(request));

	ModelAndView mv = createModelAndView(command, request, resultat);

	mv.addObject("editable", request.getSession().getAttribute("editable"));
	mv.addObject("lstHeaders", request.getSession().getAttribute("lstHeaders"));
	mv.addObject("permisSelected", command.getRechercheForm().getPermisId());
	mv.addObject("centreSelected", command.getRechercheForm().getCexNumero());
	mv.addObject(Constants.MESSAGES_MODEL_TXT, resultatMsgTxt);
	return mv;
    }

    protected ModelAndView createModelAndView(Object command, CriteresRechercheTxtView criteres, RechercheTxt criteresSelection, PlanningSemTxtView resultats) {
	ModelAndView mv = createModelAndView(command, criteres);
	((RechercheTxtCompositeForm) command).setRechercheForm(criteresSelection);
	mv.addObject(Constants.RESULTATS_MODEL_TXT, resultats);
	return mv;

    }

    protected ModelAndView createModelAndView(Object command, HttpServletRequest request, PlanningSemTxtView resultats) {
	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	RechercheTxt criteresSelection = (RechercheTxt) request.getSession().getAttribute(Constants.CRITERES_FORM_TXT);
	return createModelAndView(command, criteres, criteresSelection, resultats);

    }

    public ModelAndView handleBindException(HttpServletRequest request, HttpServletResponse response, ServletRequestBindingException bindingException) {
	LOGGER.warn("binding problem : " + bindingException);
	BindException bindException = (BindException) bindingException.getRootCause();

	bindException.reject("form.incorrect.data");

	ModelAndView mv = new ModelAndView(getPlanningViewName());
	mv.addAllObjects(bindException.getModel());
	CriteresRechercheTxtView criteres = (CriteresRechercheTxtView) request.getSession().getAttribute(Constants.CRITERES_MODEL_TXT);
	mv.addObject(Constants.CRITERES_MODEL_TXT, criteres);

	PlanningSemTxtView resultat = (PlanningSemTxtView) request.getSession().getAttribute(Constants.RESULTATS_MODEL_TXT);
	request.getSession().setAttribute(Constants.RESULTATS_MODEL_TXT, resultat);
	mv.addObject(Constants.RESULTATS_MODEL_TXT, resultat);

	RechercheTxtCompositeForm command1 = (RechercheTxtCompositeForm) bindException.getModel().get("command");
	RechercheTxt criteresSelection = (RechercheTxt) request.getSession().getAttribute(Constants.CRITERES_FORM_TXT);
	command1.setRechercheForm(criteresSelection);

	mv.addObject("editable", request.getSession().getAttribute("editable"));
	return mv;

    }

}
