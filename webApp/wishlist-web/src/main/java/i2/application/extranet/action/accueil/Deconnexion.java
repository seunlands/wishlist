package i2.application.extranet.action.accueil;

import i2.application.cerbere.commun.Cerbere;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/** Controleur 
 * 	
 *  Deconnexion
 * 
 * @author Erik Lenoir
 *
 */

public class Deconnexion extends SimpleFormController {

	/** Méthode destinée à déconnecter l'utilisateur
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// Invalidation de la session
		request.getSession(false).invalidate();

		// Deconnexion de cerbere et renvoi sur celui-ci
		Cerbere c = Cerbere.creation(request);
		if(c != null){
			 c.logoff(request, response);
		}
		return null;
	}
}