package i2.application.extranet.action.erreurs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * Controleur de gestion des 
 * erreurs d'authorisations insuffisantes
 * @author Bull
 */
public class ErreurDroit extends MultiActionController {

	static Log log = LogFactory.getLog(ErreurDroit.class);

	/* Vue(s) */
	private String viewErreurDroit;
	
	public void setViewErreurDroit(String viewErreurDroit) {
		this.viewErreurDroit = viewErreurDroit;
	}
	
//	private final String ERREUR_DROIT="Vous n'avez pas le droit d'accéder à cette fonctionnalité";
	
	public ModelAndView droit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// Récupération des informations
//		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
//		java.sql.Timestamp sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
		String urlCompleteAppelante = request.getHeader("Referer");
		if (urlCompleteAppelante==null) urlCompleteAppelante="URL INTROUVABLE";
		if(logger.isDebugEnabled()){
			logger.debug("URL non autorisée pour l'utilisateur : "+urlCompleteAppelante);
		}
//		String regex = ".*(/rafael.*)";
//		Matcher matcher = Pattern.compile(regex).matcher(urlCompleteAppelante);
		// On log dans la table
		
		return new ModelAndView(viewErreurDroit);
	}

	
}