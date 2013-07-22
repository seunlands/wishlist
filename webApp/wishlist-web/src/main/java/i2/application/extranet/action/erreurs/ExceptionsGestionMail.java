package i2.application.extranet.action.erreurs;

import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import csb.common.tools.network.EmailSender;

/**
 * Controleur de gestion d'envoie des mails d'erreur
 * 
 * @author Bull
 * 
 */
public class ExceptionsGestionMail extends MultiActionController {

    private static Log logger = LogFactory.getLog(ExceptionsGestionMail.class);

    /* Vue(s) */
    private String viewAccueil;

    public void setViewAccueil(String viewAccueil) {
	this.viewAccueil = viewAccueil;
    }

    /* Serveur mail */
    private EmailSender emailSender;

    public void setEmailSender(EmailSender emailSender) {
	this.emailSender = emailSender;
    }

    /* Parametres */
    private final String PARAM_ENVOYER = "envoyer";
    private final String PARAM_PAGE = "pageException";
    private final String PARAM_COMMENTAIRE = "commentaire";

    /* Par défaut on redirige sur la page d'accueil de l'application */
    public ModelAndView redirige(HttpServletRequest request, HttpServletResponse response) throws Exception {
	if (request.getParameter(PARAM_ENVOYER) != null) {
	    String methode = request.getParameter(PARAM_ENVOYER);
	    if (methode.equals("Envoyer")) {
		String titre = "", contenu = "";
		if (!StringUtils.isEmpty(request.getParameter(PARAM_PAGE)))
		    titre = request.getParameter(PARAM_PAGE);
		if (!StringUtils.isEmpty(request.getParameter(PARAM_COMMENTAIRE)))
		    contenu = request.getParameter(PARAM_COMMENTAIRE);
		return envoyer(request, response, titre, contenu);
	    }
	}
	return new ModelAndView(new RedirectView(viewAccueil, true));
    }

    /* Envoi du mail */
    private ModelAndView envoyer(HttpServletRequest request, HttpServletResponse response, String titre, String contenu) throws Exception {
	ViewUtilisateur user = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);

	/* Préparation du mail */
	SimpleMailMessage msg = new SimpleMailMessage();
	String erreur = request.getParameter("exceptionReader");
	String emetteur = "admin@extranet-robot.net";
	String recepteur = "pascal.fracaros@bull.net";
	List<String> destinataireLst = new ArrayList<String>();
	destinataireLst.add(recepteur);
	String subject = ("Exception signalée par : " + user.getEmail());
	String message = ("Rubrique concernée : " + titre + "\nCommentaire : " + contenu + "\n\nDétail de l'erreur : " + erreur);
	if (logger.isDebugEnabled()) {
	    logger.debug("ENVOYER MAIL " + msg.getSubject() + " " + msg.getText() + " FROM " + msg.getFrom() + " TO " + Arrays.toString(msg.getTo()));
	}
	emailSender.sendMail(subject, destinataireLst, message);
	/* On revient sur la page d'accueil */
	return new ModelAndView(new RedirectView(viewAccueil, true));
    }
}