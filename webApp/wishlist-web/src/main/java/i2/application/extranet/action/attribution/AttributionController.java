package i2.application.extranet.action.attribution;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.AbstractPlanningController;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.attribution.AttributionSemaineBean;
import i2.application.extranet.bean.view.attribution.LigneAttributionBean;
import i2.application.extranet.business.attribution.IAttributionManager;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.exceptions.DataValidateWarning;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.enums.EnumTypeEtablissement;
import i2.application.extranet.form.attribution.AttributionForm;
import i2.application.extranet.form.reservation.ReservationForm;
import i2.application.extranet.reports.attribution.GenerateAttribution;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;
import i2.application.extranet.utils.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
 * Contrôleur d'actions pour le tableau d'attribution des unites
 * 
 * @author Bull
 * 
 */
public class AttributionController extends AbstractPlanningController {

    private final static Logger LOGGER = Logger.getLogger(AttributionController.class);

    private IAttributionManager attributionManager;

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
    public AttributionController() {
	super.setModeAccessibilite(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	LOGGER.info("... Chargement de lisAnneeMois, dateLimiteReservation, DroitMensuel ");
	ModelAndView mv = super.init(request, response);

	Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);

	JSONObject jsonObj = (JSONObject) modele.get(getConstantForJson());

	if (jsonObj != null) {
	    String anneeMois = ((JSONObject) jsonObj.get(Constants.MOIS_SELECTION)).getString("id");

	    if (anneeMois != null) {
		MonthForCalendar moisExamen = new MonthForCalendar(anneeMois);
		// Traitement : Récupérer la dateLimite pour réservation
		Calendar dateClotureReserv = attributionManager.getDateLimite(getDepartementConnected(request), moisExamen.getAnneeMois(), getProfilUserConnected(request));

		if (dateClotureReserv != null) {
		    // Format de la date comme : Mercredi 11 Avril 2012
		    Locale locale = Locale.getDefault();
		    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
		    request.setAttribute("dateClotureAttribu", dateFormat.format(dateClotureReserv.getTime()));
		}
		boolean history = false;
		boolean isTTEnCours = false;
		boolean isEditable = attributionManager.isEditable(getDepartementConnected(request), moisExamen.getAnneeMois(), getProfilUserConnected(request));
		if (!isEditable & EnumProfils.LECTEUR.getLibelle().equals(getProfilUserConnected(request))) {
		    history = !attributionManager.isEditable(getDepartementConnected(request), moisExamen.getAnneeMois(), EnumProfils.REPARTITEUR.getLibelle());
		} else {
		    history = !isEditable;
		}
		if (!history) {
		    isTTEnCours = attributionManager.isTableauTravailEnCoursModification(moisExamen.getAnneeMois(), getDepartementConnected(request));
		}
		request.setAttribute("isTTEnCours", isTTEnCours);

		request.getSession().setAttribute(Constants.SES_PLANNING, this.lstLignesBeans);

		// Mise à jour du JSON dans le modèle
		modele.put(getConstantForJson(), jsonObj.toString());
	    }
	}
	return mv;
    }

    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AttributionForm command) throws ApplicationException {
	AsyncResponse servletResponse = ActionFactory.createResponse(response);

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	// filtrer les profils admin et lecteur
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {
	    try {
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
		} catch (ApplicationException e) {
		    LOGGER.error(e.getMessage(), e);
		    servletResponse.sendFail();
		    throw e;
		}
	    } catch (IOException e) {
		LOGGER.error(e.getMessage(), e);
		throw new TechnicalError(e.getMessage());
	    }
	}
	return null;
    }

    /**
     * @param attributionManager
     *            the attributionManager to set
     */
    public void setAttributionManager(IAttributionManager attributionManager) {
	this.attributionManager = attributionManager;
    }

    @SuppressWarnings("unchecked")
    public ModelAndView exportArray(HttpServletRequest request, HttpServletResponse response, ReservationForm command) throws Exception {
	ModelAndView mv = super.init(request, response);
	Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);
	List<LigneAttributionBean> listePlanning = (List<LigneAttributionBean>) modele.get(AbstractPlanningController.KEY_MODELE_LISTE_PLANNING);

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

	List<AttributionSemaineBean> list = generateAttributionSemaineBean(listePlanning, cal);

	Map<String, Object> parameters = new HashMap<>();
	parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
	parameters.put("titre", jasperBundle.getString("report.attribution.titre"));

	AutoEcole ae = getAutoEcoleConnected(request);
	String codeAEToJasper = ae.getAueCodeRafael();
	String codeAEToFile = ae.getAueCodeRafael();
	if (ae.getTypeEtab().getTypeId() != EnumTypeEtablissement.NORMAL.getType() && ae.getTypeEtab().getTypeId() != EnumTypeEtablissement.INDIVIDUELLE.getType()) {
	    codeAEToJasper += " (" + ae.getTypeEtab().getTypeLibelle() + ") ";
	    codeAEToFile += "_" + ae.getTypeEtab().getTypeLibelle() + "";
	}

	parameters.put("ecole", codeAEToJasper);
	parameters.put("mois", viewAnneeMoisSelection.getLibelle());
	InputStream input = getClass().getClassLoader().getResourceAsStream(jasperBundle.getString("report.attribution.jasper"));
	byte[] reportAttribution = GenerateAttribution.createAttributionUnites(input, parameters, list);

	String fileName = MessageFormat.format(jasperBundle.getString("report.attribution.name"), codeAEToFile, viewAnneeMoisSelection.getId());
	InputStream is = new ByteArrayInputStream(reportAttribution);
	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	response.setContentType("application/vnd.ms-excel");
	IOUtils.copy(is, response.getOutputStream());
	response.flushBuffer();
	return null;
    }

    private List<AttributionSemaineBean> generateAttributionSemaineBean(List<LigneAttributionBean> lst, Calendar cal) {
	List<AttributionSemaineBean> result = new ArrayList<>();

	List<String> lstHeaders = DateFormater.getSemaineHeader(cal);

	if (lst == null)
	    return result;
	for (LigneAttributionBean item : lst) {
	    AttributionSemaineBean i;
	    if (lstHeaders.get(0) != null) {
		i = new AttributionSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(0), item.getRetenueSemaine1(), item.getSemaine1(), item.getRetenueMensuelle(),
			item.getDifferenceRetProg1());
		result.add(i);
	    }
	    i = new AttributionSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(1), item.getRetenueSemaine2(), item.getSemaine2(), 0, item.getDifferenceRetProg2());
	    result.add(i);
	    i = new AttributionSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(2), item.getRetenueSemaine3(), item.getSemaine3(), 0, item.getDifferenceRetProg3());
	    result.add(i);
	    i = new AttributionSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(3), item.getRetenueSemaine4(), item.getSemaine4(), 0, item.getDifferenceRetProg4());
	    result.add(i);
	    if (lstHeaders.size() > 4) {
		i = new AttributionSemaineBean(item.getCentre(), item.getPermis(), lstHeaders.get(4), item.getRetenueSemaine5(), item.getSemaine5(), 0, item.getDifferenceRetProg5());
		result.add(i);
	    }
	}

	return result;
    }

}
