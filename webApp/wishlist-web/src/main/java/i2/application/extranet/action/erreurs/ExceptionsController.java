package i2.application.extranet.action.erreurs;

import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.utils.Constants;
import i2.application.extranet.utils.ExceptionsUtil;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.util.HtmlUtils;

/**
 * Controlleur de gestion des erreurs applicatives
 * 
 * @author Bull
 * 
 */
public class ExceptionsController extends SimpleMappingExceptionResolver {

    static Log logger = LogFactory.getLog(ExceptionsController.class);

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

	// Récupération des informations
	logger.debug("resolveException");
	if (ex != null) {
	    logger.error("Exception non journalisée : " + ex);
	    if (logger.isDebugEnabled()) {
		ViewUtilisateur utilisateur = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
		if (utilisateur != null)
		    logger.debug("Utilisateur " + utilisateur.getEmail() + " " + utilisateur.getInformations());
		logger.debug(ex.getMessage(), ex);
		logger.debug(ExceptionsUtil.getMessageForException(ex));
	    }
	}
	StringWriter sw = new StringWriter();
	if (ex != null) {
	    ex.printStackTrace(new java.io.PrintWriter(sw));
	}
	request.setAttribute("exceptionReader", HtmlUtils.htmlEscape(sw.toString()));

	// On laisse la méthode mère gérer le reste
	return super.resolveException(request, response, handler, ex);
    }

}
