package i2.application.extranet.action.option;

import i2.application.extranet.action.AbstractPlanningController;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.business.option.IValidationOptionManager;
import i2.application.extranet.form.option.AttribuerUnitesSuppForm;
import i2.application.extranet.form.option.ValiderOptionForm;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.network.EmailSender;

/**
 * controller de la partie ihm validation des options
 * 
 * @author bull
 * 
 */
// FIXME à supprimer
public class ValidationOptionTxtController extends AbstractPlanningController {

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
	// @SuppressWarnings("unchecked")
	// Map<String, Object> modele = (Map<String, Object>) mv.getModel().get(Constants.MODELE);
	// JSONObject jsonObj = (JSONObject) modele.get(getConstantForJson());
	//
	// String permisSelection = request.getParameter("permisSelection");
	// String centreSelection = request.getParameter("centreSelection");
	// if (permisSelection == null) {
	// permisSelection = "(Tous)";
	// }
	// if (centreSelection == null) {
	// centreSelection = "(Tous)";
	// }
	//
	// List<ViewAnneeMois> listAnneeMois = validationOptionManager.getListAnneeMois(getAutoEcoleConnected(request).getDepartement(), getProfilUserConnected(request));
	// String moisSelection = request.getParameter("moisSelection");
	// ViewAnneeMois viewAnneeMoisSelection = listAnneeMois.get(0);
	// if (moisSelection != null) {
	// for (ViewAnneeMois item : listAnneeMois) {
	// if (item.getId().equals(moisSelection)) {
	// viewAnneeMoisSelection = item;
	// }
	// }
	// }
	// Integer dateCloture = validationOptionManager.getClotureValideOption();
	// if (dateCloture != null) {
	// request.setAttribute("dateClotureOption", dateCloture);
	// }
	// // Mise à jour du json
	// jsonObj = ActionFactory.addListMois(jsonObj, listAnneeMois, viewAnneeMoisSelection);
	// jsonObj = ActionFactory.addCentreEtPermisSelection(jsonObj, permisSelection, centreSelection);
	// modele.put(getConstantForJson(), jsonObj.toString());
	return mv;
    }

    public ModelAndView getData(@SuppressWarnings("unused") HttpServletRequest request, HttpServletResponse response, ValiderOptionForm form) throws Exception {
	// List<ViewAEOptionClassement> classementAUE = new ArrayList<ViewAEOptionClassement>();
	// List<ViewAffUnitesSuppCreneauHoraire> lstCreno = new ArrayList<ViewAffUnitesSuppCreneauHoraire>();
	// lstCreno.addAll(validationOptionManager.getCreneauHoraire(form.getIds()));
	// classementAUE.addAll(validationOptionManager.getClassementAue(form.getIds()));
	// JSONObject jsonObj = new JSONObject();
	// JSONArray json = ActionFactory.createClassement(classementAUE);
	// jsonObj.put("classement", json);
	// JSONArray jsonArray = ActionFactory.createCreneauUniteSupp(lstCreno);
	// jsonObj.put("creneau", jsonArray);
	// jsonObj.put("centre", form.getCentre());
	// jsonObj.put("salle", form.getSalle());
	// response.getWriter().append(jsonObj.toString()).flush();
	return null;
    }

    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AttribuerUnitesSuppForm form) throws Exception {
	AsyncResponse servletResponse = ActionFactory.createResponse(response);
	// ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	// String profilUserConnected = userConnected.getLibelleProfil();
	// if (userConnected.getRestriction() != null && userConnected.getRestriction() instanceof AutoEcole && !profilUserConnected.equals(EnumProfils.LECTEUR.getLibelle())) {
	// try {
	// String salle = form.getSalle();
	// String centre = form.getCentre();
	// List<Email> lstEmail = validationOptionManager.saveModifs(form.getAttributions(), ((AutoEcole) userConnected.getRestriction()).getDepartement(), userConnected.getEmail());
	// for (Email email : lstEmail) {
	// int items = email.getParamBody().length + 2;
	// Object[] params = new Object[items];
	// for (int i = 0; i < email.getParamBody().length; i++) {
	// params[i] = email.getParamBody()[i];
	// }
	// params[params.length - 1] = salle;
	// params[params.length - 2] = centre;
	// String filename = emailBundle.getString("email.attribunitessupp.template");
	// InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
	// String subject = emailBundle.getString("email.attribunitessup.sujet");
	// emailSender.sendMail(subject, email.getLstDestinaires(), LibelleUtil.getString(is, params));
	// }
	// servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
	// servletResponse.sendSuccess();
	// } catch (TechnicalError te) {
	// LOGGER.debug(te);
	// servletResponse.addMessage(messages.getMessage("msg.common.modifs.non.enregistrees"));
	// for (String m : te.getListMessage()) {
	// servletResponse.addMessage(m);
	// }
	// servletResponse.sendFail();
	// }
	// } else {
	// sendUnauthorized("submitModification", servletResponse, userConnected);
	// }
	return null;
    }

    protected void sendUnauthorized(final String action, final AsyncResponse servletResponse, final ViewUtilisateur userConnected) throws IOException {
	// servletResponse.addMessage(emailBundle.getString("action.non.autorisee"));
	// LOGGER.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	// servletResponse.sendFail();
    }

    public ValidationOptionTxtController() {
	// super.setModeAccessibilite(false);
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
