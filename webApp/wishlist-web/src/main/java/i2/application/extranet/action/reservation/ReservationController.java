package i2.application.extranet.action.reservation;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.AbstractPlanningController;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.ViewDroitMensuel;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.reservation.LigneReservationBean;
import i2.application.extranet.bean.view.reservation.ReservationSemaineBean;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.exceptions.ConcurrentDataAccessException;
import i2.application.extranet.business.exceptions.DataValidateWarning;
import i2.application.extranet.business.reservation.IReservationManager;
import i2.application.extranet.business.util.BeanViewUtil;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.enums.EnumTypeEtablissement;
import i2.application.extranet.form.reservation.ReservationForm;
import i2.application.extranet.reports.reservation.GenerateReservation;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;
import i2.application.extranet.utils.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRParameter;
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
 *         Contrôleur d'actions pour le tableau reservation d'unités
 */


public class ReservationController extends AbstractPlanningController {

    private final static Logger LOGGER = Logger.getLogger(ReservationController.class);

    private IReservationManager reservationManager;

    private ResourceBundle jasperBundle;

    public ResourceBundle getJasperBundle() {
	return jasperBundle;
    }

    public void setJasperBundle(ResourceBundle jasperBundle) {
	this.jasperBundle = jasperBundle;
    }

    /**
     * Constructeur par défaut
     */
    public ReservationController() {
	super.setModeAccessibilite(false);
    }

    /**
     * @param reservationManager
     *            the reservationManager to set
     */
    public void setReservationManager(IReservationManager reservationManager) {
	this.reservationManager = reservationManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {

	LOGGER.info("... Chargement de lisAnneeMois, dateLimiteReservation, DroitMensuel ");
	ModelAndView mv = super.init(request, response);

	Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);

	List<IViewLignePlanning> listePlanning = (List<IViewLignePlanning>) modele.get(AbstractPlanningController.KEY_MODELE_LISTE_PLANNING);

	JSONObject jsonObj = (JSONObject) modele.get(getConstantForJson());

	if (jsonObj != null) {

	    String anneeMois = ((JSONObject) jsonObj.get(Constants.MOIS_SELECTION)).getString("id");

	    if (anneeMois != null) {
		MonthForCalendar moisExamen = new MonthForCalendar(anneeMois);
		// Traitement : Récupérer la dateLimite pour réservation
		Calendar dateClotureReserv = reservationManager.getDateLimite(getDepartementConnected(request), moisExamen.getAnneeMois(), getProfilUserConnected(request));

		if (dateClotureReserv != null) {
		    Locale locale = Locale.getDefault();
		    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
		    request.setAttribute("dateClotureReserv", dateFormat.format(dateClotureReserv.getTime()));
		    LOGGER.info("... Le panneau de reservation est ouvert en edition ");
		}
		// Mise à  jour du json

		List<ViewPermis> lstPermisView = BeanViewUtil.getListGroupePermis(listePlanning);

		// Traitement : Récupérer la liste des droits mensuel
		List<ViewDroitMensuel> listDroitMensuel = reservationManager.getListDroitsMensuelPourCriteres(getAutoEcoleConnected(request), getDepartementConnected(request), moisExamen.getAnnee(),
			moisExamen.getMois(), getProfilUserConnected(request), lstPermisView);

		// Mise à  jour du JSON
		jsonObj = ActionFactory.addListDroitsMensuels(jsonObj, listDroitMensuel);
		request.getSession().setAttribute(Constants.SES_PLANNING, this.lstLignesBeans);

		// Mise à jour du JSON dans le modèle
		modele.put(getConstantForJson(), jsonObj.toString());
	    }
	}
	return mv;
    }

    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, ReservationForm command) throws Exception {
	AsyncResponse servletResponse = ActionFactory.createResponse(response);

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	// filtrer les profils admin et lecteur
	if (LOGGER.isInfoEnabled()) {
	    for (IViewLignePlanning line : command.getLignePlanning()) {
		LOGGER.info("... lignePlanning: " + ((LigneReservationBean) line).getPermis() + "  " + ((LigneReservationBean) line).getMoisAnnee() + " " + ((LigneReservationBean) line).getCentre()
			+ " nbr1= " + ((LigneReservationBean) line).getSemaine1() + " nbr2= " + ((LigneReservationBean) line).getSemaine2() + " nbr3= " + ((LigneReservationBean) line).getSemaine3()
			+ " nbr4= " + ((LigneReservationBean) line).getSemaine4() + " nbr5= " + ((LigneReservationBean) line).getSemaine5());
	    }
	}
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {
	    try {
		super.abstractSubmitModification(request, response, command);
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
		servletResponse.sendSuccess();
	    } catch (DataValidateWarning e) {
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
		for (String msg : e.getListMessage()) {
		    servletResponse.addMessage(msg);
		}
		servletResponse.sendWarn();
	    } catch (ConcurrentDataAccessException e) {
		LOGGER.error(e.getMessage(), e);
		servletResponse.sendError();
	    } catch (ApplicationException e) {
		servletResponse.sendFail();
		LOGGER.error(e.getMessage(), e);
		throw new Exception(e);
	    } catch (Exception e) {
		LOGGER.fatal(e.getMessage(), e);
		servletResponse.sendFail();
		throw e;
	    }
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    public ModelAndView exportArray(HttpServletRequest request, HttpServletResponse response, ReservationForm command) throws Exception {

	ModelAndView mv = super.init(request, response);
	Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);
	List<LigneReservationBean> listePlanning = (List<LigneReservationBean>) modele.get(AbstractPlanningController.KEY_MODELE_LISTE_PLANNING);

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

	List<ReservationSemaineBean> list = generateReservationSemaineBean(listePlanning, cal);

	Map<String, Object> parameters = new HashMap<>();
	parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
	parameters.put("titre", jasperBundle.getString("report.reservation.titre"));

	AutoEcole ae = getAutoEcoleConnected(request);
	String codeAEToJasper = ae.getAueCodeRafael();
	String codeAEToFile = ae.getAueCodeRafael();
	if (ae.getTypeEtab().getTypeId() != EnumTypeEtablissement.NORMAL.getType() && ae.getTypeEtab().getTypeId() != EnumTypeEtablissement.INDIVIDUELLE.getType()) {
	    codeAEToJasper += " (" + ae.getTypeEtab().getTypeLibelle() + ") ";
	    codeAEToFile += "_" + ae.getTypeEtab().getTypeLibelle() + "";
	}

	parameters.put("ecole", codeAEToJasper);
	parameters.put("mois", viewAnneeMoisSelection.getLibelle());
	InputStream input = getClass().getClassLoader().getResourceAsStream(jasperBundle.getString("report.reservation.jasper"));
	byte[] reportReservation = GenerateReservation.createReservationUnites(input, parameters, list);

	String fileName = MessageFormat.format(jasperBundle.getString("report.reservation.name"), codeAEToFile, viewAnneeMoisSelection.getId());
	InputStream is = new ByteArrayInputStream(reportReservation);
	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	response.setContentType("application/vnd.ms-excel");
	IOUtils.copy(is, response.getOutputStream());
	response.flushBuffer();
	return null;
    }

    private List<ReservationSemaineBean> generateReservationSemaineBean(List<LigneReservationBean> lst, Calendar cal) {
	List<ReservationSemaineBean> result = new ArrayList<>();

	List<String> lstHeaders = DateFormater.getSemaineHeader(cal);

	for (LigneReservationBean item : lst) {
	    ReservationSemaineBean i;
	    if (lstHeaders.get(0) != null) {
		i = new ReservationSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(0), item.getSemaine1());
		result.add(i);
	    }
	    i = new ReservationSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(1), item.getSemaine2());
	    result.add(i);
	    i = new ReservationSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(2), item.getSemaine3());
	    result.add(i);
	    i = new ReservationSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(3), item.getSemaine4());
	    result.add(i);
	    if (lstHeaders.size() > 4) {
		i = new ReservationSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(4), item.getSemaine5());
		result.add(i);
	    }
	}

	return result;
    }
}
