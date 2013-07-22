package i2.application.extranet.action.reservation;

import i2.application.aurige.bean.Departement;
import i2.application.extranet.action.AbstractMultiGroupingPlanningController;
import i2.application.extranet.bean.view.multigroup.AbstractViewLigneMultiGroup;
import i2.application.extranet.business.reservation.IConsultationUnitesRetenuesManager;
import i2.application.extranet.business.util.LibelleUtil;
import i2.application.extranet.form.AjustementRequestForm;
import i2.application.extranet.form.GroupeRequestForm;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.date.DateFormater;
import csb.common.tools.network.EmailSender;

/**
 * Contolleur du panneau consultation des unités retenues
 * 
 * @author Bull
 */
public class ConsultationUnitesRetenuesController extends AbstractMultiGroupingPlanningController {

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(ConsultationUnitesRetenuesController.class);

    private IConsultationUnitesRetenuesManager consultationManager;

    private EmailSender emailSender;

    private ResourceBundle emailBundle;

    @Override
    public boolean isGroupByCentreExamen() {
	return true;
    }

    @Override
    public boolean isGroupByGroupePermis() {
	return false;
    }

    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {

	if (request.getParameter("succes") != null) {
	    request.setAttribute("succes", messages.getMessage("validation.retenues.succes"));
	}

	return super.init(request, response);
    }

    @Override
    public int getNbrParPage() {
	return Integer.parseInt(getServletContext().getInitParameter("nbPermisParPage"));
    }

    @Override
    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AjustementRequestForm form) throws Exception {
	return null;
    }

    /**
     * Accomplie l'action "Valider les unités retenues"
     * 
     * @param request
     * @param response
     * @param form
     * @return
     * @throws Exception
     */
    public ModelAndView validate(HttpServletRequest request, HttpServletResponse response, GroupeRequestForm form) throws Exception {
	AsyncResponse servletResponse = ActionFactory.createResponse(response);

	String anneeMois = form.getAnneeMois();
	boolean isValidable = consultationManager.isMoisValidable(anneeMois, getDepartementConnected(request), getProfilUserConnected(request));

	if (!isValidable) {
	    servletResponse.addMessage(messages.getMessage("validation.retenues.refusees"));
	    servletResponse.sendFail();
	} else {
	    consultationManager.validateRetenues(anneeMois, getDepartementConnected(request), getMailUserConnected(request));
	    sendValidationMail(getDepartementConnected(request), anneeMois);
	    servletResponse.addMessage(messages.getMessage("validation.retenues.succes"));
	    servletResponse.sendSuccess();
	}
	return null;
    }

    /**
     * @param consultationManager
     *            the consultationManager to set
     */
    public void setConsultationManager(IConsultationUnitesRetenuesManager consultationManager) {
	this.consultationManager = consultationManager;
    }

    /**
     * @param emailSender
     *            the emailSender to set
     */
    public void setEmailSender(EmailSender emailSender) {
	this.emailSender = emailSender;
    }

    /**
     * Envoie le mail au délégué du département.
     * 
     * @param departement
     * @param anneeMois
     */
    private void sendValidationMail(Departement departement, String anneeMois) {
	List<String> mailsDelegue = consultationManager.getMailDelegue(departement);
	Object[] args = { DateFormater.formatLibelleDate(DateFormater.formatIdDate(anneeMois)), departement.getDepCode(), DateFormater.formatDate(Calendar.getInstance()) };
	String filename = emailBundle.getString("email.validation.consult.retenues.path");
	String sujet = emailBundle.getString("email.validation.consult.retenues.sujet");
	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
	emailSender.sendMail(sujet, mailsDelegue, LibelleUtil.getString(inputStream, args));
    }

    /**
     * @param emailBundle
     *            the emailBundle to set
     */
    public void setEmailBundle(ResourceBundle emailBundle) {
	this.emailBundle = emailBundle;
    }

    @Override
    public String getElementReferencePagination(AbstractViewLigneMultiGroup ligne) {
	return ligne.getPermis();
    }

    @Override
    public boolean isNotGrouped() {
	return false;
    }

}
