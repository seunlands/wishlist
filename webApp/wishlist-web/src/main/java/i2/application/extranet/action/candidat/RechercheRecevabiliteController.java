package i2.application.extranet.action.candidat;

import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.recevabilite.RecevabiliteBean;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.recevabilite.IRecevabiliteManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.recevabilite.RecevabiliteForm;
import i2.application.extranet.utils.Constants;
import i2.application.extranet.utils.WebBundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import csb.common.tools.date.DateFormater;

/**
 * Controller d'actions pour la recherche de recevabilite d'un candidat
 * 
 * @author bull
 * 
 */
public class RechercheRecevabiliteController extends SimpleFormController {

    private WebBundle messages;

    public WebBundle getMessages() {
	return messages;
    }

    public void setMessages(WebBundle messages) {
	this.messages = messages;
    }

    private IRecevabiliteManager recevabiliteManager;

    public void setRecevabiliteManager(IRecevabiliteManager recevabiliteManager) {
	this.recevabiliteManager = recevabiliteManager;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
	// init du form
	RecevabiliteForm recevabiliteForm = new RecevabiliteForm();
	recevabiliteForm.setListeEpreuve(recevabiliteManager.rechercheTypeEpreuveNonPro());
	return recevabiliteForm;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder dataBinder) throws Exception {
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	df.setLenient(false);
	dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
	dataBinder.registerCustomEditor(java.util.Date.class, null, new CustomDateEditor(df, false));
	super.initBinder(request, dataBinder);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	Map<String, Object> map = new HashMap<String, Object>();
	if (userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()) || userConnected.getLibelleProfil().equals(EnumProfils.AE.getLibelle())
		|| userConnected.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle())) {
	    // reccup du form administration
	    RecevabiliteForm recevabiliteForm = (RecevabiliteForm) command;
	    RecevabiliteBean recevabiliteBean;
	    // a decommenter si suppression du bouchon

	    // try {
	    // recevabiliteBean = recevabiliteManager.rechercheRecevabiliteCandidat(recevabiliteForm.getCanNeph(), recevabiliteForm.getTepCode(), recevabiliteForm.getDateExam());
	    //
	    // Object[] args = { DateFormater.formatDate(recevabiliteBean.getDateDebut()), DateFormater.formatDate(recevabiliteBean.getDateFin()) };
	    // map.put("msgRecOK", messages.getMessage("candidat.recevable", args, Locale.ROOT));
	    // } catch (ApplicationException e) {
	    // map.put("msgRecKO", messages.getMessage("candidat.non.recevable", null, Locale.ROOT));
	    // }

	    // création d'un bouchon pour le moment meme si on trouve le candidat recevable avec date de debut et de fin
	    map.put("msgRecIndeterminee", messages.getMessage("recevabilite.indeterminee"));
	    map.put(getCommandName(), recevabiliteForm);
	} else {
	    sendUnauthorized("onSubmit", map, userConnected);
	}
	return showForm(request, response, errors, map);
    }

    protected void sendUnauthorized(final String action, Map<String, Object> map, final ViewUtilisateur userConnected) {
	logger.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	map.put("msgInfos", messages.getMessage("action.non.autorisee"));

    }

}
