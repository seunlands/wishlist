package i2.application.extranet.action.option;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.AbstractPlanningController;
import i2.application.extranet.bean.view.Email;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.option.ViewSeanceOptionAccordee;
import i2.application.extranet.business.exceptions.ActiviteModifieeException;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.business.option.IValidationOptionManager;
import i2.application.extranet.business.util.LibelleUtil;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.option.AttribuerUnitesSuppForm;
import i2.application.extranet.form.option.ValiderOptionForm;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;
import i2.application.extranet.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.network.EmailSender;

/**
 * controller de la partie ihm validation des options
 * 
 * @author bull
 * 
 */
public class ValidationOptionController extends AbstractPlanningController {

    private final static Logger LOGGER = Logger.getLogger(OptionPoseController.class);

    private IValidationOptionManager validationOptionManager;

    private ResourceBundle emailBundle;

    /* Serveur mail */
    private EmailSender emailSender;

    public EmailSender getEmailSender() {
	return emailSender;
    }

    public void setEmailSender(EmailSender emailSender) {
	this.emailSender = emailSender;
    }

    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("Enter init() de ValidationOptionController");
	}
	ModelAndView mv = super.init(request, response);
	@SuppressWarnings("unchecked")
	Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);
	JSONObject jsonObj = (JSONObject) modele.get(getConstantForJson());
	if (jsonObj != null) {
	    Integer dateCloture = validationOptionManager.getClotureValideOption();
	    if (dateCloture != null) {
		request.setAttribute("dateClotureOption", dateCloture);
	    }
	}
	return mv;
    }

    public ModelAndView getData(HttpServletRequest request, HttpServletResponse response, ValiderOptionForm form) throws Exception {

	ViewSeanceOptionAccordee viewSeance = validationOptionManager.getAffichagePlanning(form.getIds(), this.getDepartementConnected(request));
	JSONObject jsonObj = new JSONObject();
	JSONArray json = ActionFactory.createClassement(viewSeance.getClassement());
	jsonObj.put("classement", json);
	JSONArray jsonArray = ActionFactory.createCreneauUniteSupp(viewSeance.getCreneauxHoraire());
	jsonObj.put("creneau", jsonArray);
	jsonObj.put("centre", form.getCentre());
	jsonObj.put("salle", form.getSalle());
	jsonObj.put("salleId", form.getSalleId());
	jsonObj.put("jour", form.getJour());
	jsonObj.put("cjo", form.getCjo());
	response.getWriter().append(jsonObj.toString()).flush();
	return null;
    }

    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AttribuerUnitesSuppForm form) throws Exception {
	AsyncResponse servletResponse = ActionFactory.createResponse(response);
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	String profilUserConnected = userConnected.getLibelleProfil();
	if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !profilUserConnected.equals(EnumProfils.LECTEUR.getLibelle())) {
	    try {
		// construction map act-timestamp
		Map<Integer, Long> mapActTime = new HashMap<Integer, Long>();
		for (int i = 0; i < form.getActIds().size(); i++) {
		    mapActTime.put(form.getActIds().get(i), form.getTimestamps().get(i));
		}
		List<Email> lstEmail = validationOptionManager.saveModifs(form.getAttributions(), ((AutoEcole) userConnected.getRestriction()).getDepartement(), userConnected.getEmail(), mapActTime);
		for (Email email : lstEmail) {

		    String filename = null;
		    if (((String) email.getParamBody()[0]).equals("0")) {
			filename = emailBundle.getString("email.attribunitessupp.annulation.template");
		    } else {
			filename = emailBundle.getString("email.attribunitessupp.template");
		    }
		    InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
		    String subject = emailBundle.getString("email.attribunitessup.sujet");
		    emailSender.sendMail(subject, email.getLstDestinaires(), LibelleUtil.getString(is, email.getParamBody()));
		}
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
		servletResponse.sendSuccess();
	    } catch (TechnicalError te) {
		LOGGER.debug(te);
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.non.enregistrees"));
		for (String m : te.getListMessage()) {
		    servletResponse.addMessage(m);
		}
		servletResponse.sendFail();
	    } catch (ActiviteModifieeException ex) {
		LOGGER.debug(ex);
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.non.enregistrees"));
		servletResponse.addMessage(messages.getMessage("accordees.options.concurrence"));
		servletResponse.sendError();

	    }
	} else {
	    sendUnauthorized("submitModification", servletResponse, userConnected);
	}
	return null;
    }

    protected void sendUnauthorized(final String action, final AsyncResponse servletResponse, final ViewUtilisateur userConnected) throws IOException {
	servletResponse.addMessage(emailBundle.getString("action.non.autorisee"));
	LOGGER.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	servletResponse.sendFail();
    }

    public ValidationOptionController() {
	super.setModeAccessibilite(false);
    }

    public void setValidationOptionManager(IValidationOptionManager validationOptionManager) {
	this.validationOptionManager = validationOptionManager;
    }

    public ResourceBundle getEmailBundle() {
	return emailBundle;
    }

    public void setEmailBundle(ResourceBundle emailBundle) {
	this.emailBundle = emailBundle;
    }

}
