package i2.application.extranet.action.affectation;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.AbstractPlanningController;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.affectation.AffectationCreneauHoraire;
import i2.application.extranet.bean.view.affectation.AffectationJourBean;
import i2.application.extranet.bean.view.affectation.AffectationSeanceDetail;
import i2.application.extranet.bean.view.affectation.ViewRecapitulatifHebdomadaire;
import i2.application.extranet.bean.view.planningjour.AbstractSeance;
import i2.application.extranet.bean.view.planningjour.LignePlanningJourBean;
import i2.application.extranet.bean.view.planningjour.PlanningJour;
import i2.application.extranet.business.affectation.IAffectationManager;
import i2.application.extranet.business.exceptions.DataValidateError;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.enums.EnumTypeEtablissement;
import i2.application.extranet.form.affetation.AffectationCreneauForm;
import i2.application.extranet.form.affetation.AffectationDetailForm;
import i2.application.extranet.form.affetation.AffectationForm;
import i2.application.extranet.form.reservation.ReservationForm;
import i2.application.extranet.reports.affectation.GenerateAffectation;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;
import i2.application.extranet.utils.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.date.DateFormater;
import csb.common.tools.date.MonthForCalendar;

/**
 * 
 * @author BULL SAS
 * 
 *         Contrôleur d'actions pour le tableau des unités affectées
 * 
 */
public class AffectationController extends AbstractPlanningController {

    private final static Logger logger = Logger.getLogger(AffectationController.class);

    protected IAffectationManager affectationManager;

    private ResourceBundle jasperBundle;

    public ResourceBundle getJasperBundle() {
	return jasperBundle;
    }

    public void setJasperBundle(ResourceBundle jasperBundle) {
	this.jasperBundle = jasperBundle;
    }

    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {

	logger.debug("#init");

	ModelAndView mv = null;
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole) {

	    mv = super.init(request, response);

	    @SuppressWarnings("unchecked")
	    Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);
	    JSONObject jsonObj = (JSONObject) modele.get(getConstantForJson());
	    if (jsonObj != null) {
		String anneeMois = ((JSONObject) jsonObj.get(Constants.MOIS_SELECTION)).getString("id");
		MonthForCalendar moisExamen = new MonthForCalendar(anneeMois);

		@SuppressWarnings("unchecked")
		List<IViewLignePlanning> lstLignesBeans = (List<IViewLignePlanning>) modele.get("lstLignesBeans");
		List<ViewRecapitulatifHebdomadaire> list = new ArrayList<>();
		if (!lstLignesBeans.isEmpty()) {
		    list = affectationManager.getListRecapitulatifPourCriteres(getAutoEcoleConnected(request), moisExamen.getAnnee(), moisExamen.getMois());
		}
		jsonObj = ActionFactory.addRecapitulatifAffectations(jsonObj, list);

		{
		    Integer dateCloture = affectationManager.getCloture();
		    request.setAttribute("dateCloture", dateCloture);
		}
		modele.put(getConstantForJson(), jsonObj.toString());
	    }

	} else {
	    AsyncResponse servletResponse = ActionFactory.createResponse(response);
	    sendUnauthorized("init", servletResponse, userConnected);
	}
	return mv;

    }

    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AffectationForm form) throws IOException {
	if (logger.isDebugEnabled()) {
	    logger.debug("#submitModification : form = " + form);
	}

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);

	AsyncResponse servletResponse = ActionFactory.createResponse(response);
	// filtrer les profils admin et lecteur
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {

	    try {
		boolean fail = false;
		AutoEcole aue = (AutoEcole) userConnected.getRestriction();
		String profil = getProfilUserConnected(request);

		// Traitement lors de l'action recherche Etbs
		if (form.getAueCodeSelected() != null && (profil.equals(EnumProfils.REPARTITEUR.getLibelle()) || profil.equals(EnumProfils.LECTEUR.getLibelle()))) {
		    aue = getAutoEcoleManager().readAutoEcole(form.getAueCodeSelected());
		}

		List<AffectationCreneauHoraire> affectationList = new ArrayList<>();
		for (AffectationCreneauForm affectationCreneauForm : form.getAffectations()) {
		    AffectationCreneauHoraire affectation = convertAffectationForm(affectationCreneauForm);
		    if (affectation != null) {
			affectationList.add(affectation);
		    }
		}
		Map<Integer, Long> mapActTime = new HashMap<Integer, Long>();
		for (int i = 0; i < form.getActIds().size(); i++) {
		    mapActTime.put(form.getActIds().get(i), form.getTimestamps().get(i));
		}
		try {
		    if (aue != null) {
			affectationManager.saveAffectation(aue, affectationList, userConnected.getEmail(), mapActTime);
		    }
		} catch (DataValidateError e) {
		    logger.debug(e);
		    form = new AffectationForm();
		    for (String m : e.getListMessage()) {
			servletResponse.addMessage(m);
		    }
		    fail = true;
		} catch (TechnicalError e) {
		    logger.warn(e);
		    for (String m : e.getListMessage()) {
			servletResponse.addMessage(m);
		    }
		    fail = true;
		}
		if (fail) {
		    servletResponse.sendError();
		} else {
		    servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
		    servletResponse.sendSuccess();
		}

	    } catch (IOException e) {
		logger.warn("#submitModification exception", e);
		servletResponse.sendFail();
	    }

	} else {
	    sendUnauthorized("submitModification", servletResponse, userConnected);
	}
	return null;

    }

    public ModelAndView getData(HttpServletRequest request, HttpServletResponse response, AffectationDetailForm form) throws Exception {

	if (logger.isDebugEnabled())
	    logger.debug("AffectationsDetailForm=" + form);

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole) {
	    AutoEcole aue = (AutoEcole) userConnected.getRestriction();

	    String profil = getProfilUserConnected(request);
	    // Traitement lors de l'action recherche Etbs
	    if (form.getAueCodeSelected() != null && (profil.equals(EnumProfils.REPARTITEUR.getLibelle()) || profil.equals(EnumProfils.LECTEUR.getLibelle()))) {
		aue = getAutoEcoleManager().readAutoEcole(form.getAueCodeSelected());
	    }

	    AffectationSeanceDetail data = null;
	    if (aue != null) {
		data = affectationManager.getAffectationsByActiviteAndCreneau(aue, form.getIds(), userConnected.getLibelleProfil());
		logger.debug("data=" + data.getPlacesDispo());
	    } else {
		data = affectationManager.getAffectationsByActiviteAndCreneau((AutoEcole) userConnected.getRestriction(), form.getIds(), userConnected.getLibelleProfil());
		logger.debug("data=" + data.getPlacesDispo());
	    }
	    JSONObject json = ActionFactory.createAffichageAcitivtePlanningDetail(data);
	    response.getWriter().append(json.toString()).flush();

	} else {
	    AsyncResponse servletResponse = ActionFactory.createResponse(response);
	    sendUnauthorized("getData", servletResponse, userConnected);
	}
	return null;

    }

    protected void sendUnauthorized(final String action, final AsyncResponse servletResponse, final ViewUtilisateur userConnected) throws IOException {
	servletResponse.addMessage(messages.getMessage("action.non.autorisee"));
	logger.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	servletResponse.sendFail();
    }

    /**
     * @param affectationsManager
     *            the affectationsManager to set
     */
    public void setAffectationManager(IAffectationManager affectationManager) {
	this.affectationManager = affectationManager;
    }

    protected AffectationCreneauHoraire convertAffectationForm(AffectationCreneauForm affectationCreneauForm) {
	AffectationCreneauHoraire affectation = null;
	if (affectationCreneauForm.getActId() != null) {
	    affectation = new AffectationCreneauHoraire();
	    affectation.setActId(affectationCreneauForm.getActId());
	    affectation.setIndex(affectationCreneauForm.getIndex());
	    affectation.setNbPlaceResa(affectationCreneauForm.getNbUnites());
	}
	return affectation;
    }

    @SuppressWarnings("unchecked")
    public ModelAndView exportArray(HttpServletRequest request, HttpServletResponse response, ReservationForm command) throws Exception {

	ModelAndView mv = super.init(request, response);
	Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);
	List<LignePlanningJourBean> listePlanning = (List<LignePlanningJourBean>) modele.get(AbstractPlanningController.KEY_MODELE_LISTE_PLANNING);
	Calendar cal = Calendar.getInstance();

	List<ViewAnneeMois> listAnneeMois = getPlanningManager().getListAnneeMois(getDepartementConnected(request), getProfilUserConnected(request));
	String moisSelection = request.getParameter("moisSelection");
	ViewAnneeMois viewAnneeMoisSelection = listAnneeMois.get(0);

	if (moisSelection != null) {
	    for (ViewAnneeMois item : listAnneeMois) {
		if (item.getId().equals(moisSelection)) {
		    viewAnneeMoisSelection = item;
		}
	    }
	}
	cal.setTime(DateFormater.formatIdDate(viewAnneeMoisSelection.getId()));

	List<AffectationJourBean> list = generateAffectationJourBean(listePlanning, cal);
	Map<String, Object> parameters = new HashMap<>();

	AutoEcole ae = getAutoEcoleConnected(request);
	String codeAEToJasper = ae.getAueCodeRafael();
	String codeAEToFile = ae.getAueCodeRafael();
	if (ae.getTypeEtab().getTypeId() != EnumTypeEtablissement.NORMAL.getType() && ae.getTypeEtab().getTypeId() != EnumTypeEtablissement.INDIVIDUELLE.getType()) {
	    codeAEToJasper += " (" + ae.getTypeEtab().getTypeLibelle() + ") ";
	    codeAEToFile += "_" + ae.getTypeEtab().getTypeLibelle() + "";
	}

	parameters.put("ecole", codeAEToJasper);
	parameters.put("mois", viewAnneeMoisSelection.getLibelle());
	InputStream input = getClass().getClassLoader().getResourceAsStream(jasperBundle.getString("report.affectation.jasper"));
	byte[] reportAffectation = GenerateAffectation.createAffectationUnites(input, parameters, list);

	String fileName = MessageFormat.format(jasperBundle.getString("report.affectation.name"), codeAEToFile, viewAnneeMoisSelection.getId());
	InputStream is = new ByteArrayInputStream(reportAffectation);
	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	response.setContentType("application/vnd.ms-excel");
	IOUtils.copy(is, response.getOutputStream());
	response.flushBuffer();

	return null;
    }

    private List<AffectationJourBean> generateAffectationJourBean(List<LignePlanningJourBean> listePlanning, Calendar date) {
	List<AffectationJourBean> list = new ArrayList<>();
	SimpleDateFormat formatJour = new SimpleDateFormat("d");

	Calendar dateFin = Calendar.getInstance();
	dateFin.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.getActualMaximum(Calendar.DAY_OF_MONTH));

	List<String> joursHeader = DateFormater.getJoursHeader(date, dateFin);

	for (LignePlanningJourBean item : listePlanning) {
	    List<AffectationJourBean> listeBean = new ArrayList<>();
	    for (String jour : joursHeader) {
		for (int i = 0; i < 4; i++) {
		    AffectationJourBean bean = new AffectationJourBean();
		    bean.setCentre(item.getCentre());
		    bean.setDate(jour);
		    bean.setSalle(item.getSalle());
		    bean.setCodeJour(String.valueOf(i + 1));
		    bean.setContenu("");
		    listeBean.add(bean);
		}
	    }
	    for (PlanningJour jour : item.getLstPlanningJour()) {

		for (AbstractSeance seance : jour.getLstSeances()) {
		    int index = (Integer.parseInt(formatJour.format(jour.getDate().getTime())) - 1) * 4 + Integer.parseInt(seance.getCodeJour().equals("J") ? "1" : seance.getCodeJour()) - 1;
		    AffectationJourBean bean = listeBean.get(index);
		    bean.setCentre(item.getCentre());
		    bean.setSalle(item.getSalle());

		    bean.setContenu(bean.getContenu() + "  " + seance.getPermis() + " : " + seance.getNbPlaceResa() + " (" + seance.getPlacePrises() + " / " + seance.getPlacesTotal() + ")");
		    bean.setCodeJour(seance.getCodeJour().equals("J") ? "1" : seance.getCodeJour());
		    listeBean.set(index, bean);
		}
	    }
	    list.addAll(listeBean);
	}

	return list;
    }
}
