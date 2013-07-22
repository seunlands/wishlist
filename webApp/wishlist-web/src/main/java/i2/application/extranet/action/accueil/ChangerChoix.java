package i2.application.extranet.action.accueil;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.ViewUtilisateur.Choix;
import i2.application.extranet.business.authentification.IAutoEcoleManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Classe permettant de changer le choix de l'utilisateur en cas de multi-dep ou multi-ae
 * 
 * @author Erik Lenoir
 * 
 */
public class ChangerChoix implements Controller {

    private IAutoEcoleManager autoEcoleManager;
    private String urlAccueil;

    // Injecteurs
    public void setAutoEcoleManager(IAutoEcoleManager autoEcoleManager) {
	this.autoEcoleManager = autoEcoleManager;
    }

    public void setUrlAccueil(String urlAccueil) {
	this.urlAccueil = urlAccueil;
    }

    // Parametres
    private final String PARAM_CHOIX = "choix";

    private final String PARAM_THEME = "displayTheme";

    private final String PARAM_VALUE_THEME_INTRANET = "intranet";

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

	if (request.getParameter(PARAM_THEME) != null && !request.getParameter(PARAM_THEME).isEmpty()) {
	    String theme = (String) request.getParameter(PARAM_THEME);
	    if (!PARAM_VALUE_THEME_INTRANET.equals(theme)) {
		request.getSession().setAttribute(Constants.SES_THEME_TEXTE, Boolean.TRUE);
		request.getSession().setAttribute(Constants.SES_PATH_TEXTE, "/txt");
	    } else {
		request.getSession().setAttribute(Constants.SES_THEME_TEXTE, Boolean.FALSE);
		request.getSession().setAttribute(Constants.SES_PATH_TEXTE, "");
	    }

	} else if (request.getParameter(PARAM_CHOIX) != null && !request.getParameter(PARAM_CHOIX).isEmpty()) {
	    // Récupération du choix et de l'utilisateur
	    String choix = (String) request.getParameter(PARAM_CHOIX);
	    ViewUtilisateur user = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	    int index = 0;
	    // Parcours des choix
	    for (Choix ch : user.getChoix()) {
		if (ch.getId().equals(choix)) {
		    // Changement du choix par défaut
		    user.setChoixParDefaut(index);
		    String libelleProfil = user.getLibelleProfil();
		    // MAJ de la restriction correspondante
		    if (!libelleProfil.equals(EnumProfils.ADMIN.getLibelle())) {
			AutoEcole ae = autoEcoleManager.readAutoEcole(ch.getId());
			user.setRestriction(autoEcoleManager.readAutoEcole(ch.getId()));
			user.setDepartement(ae.getDepartement());
		    }
		    // On remet l'user en session
		    request.getSession().setAttribute(Constants.SES_UTILISATEUR, user);
		    break;
		}
		index++;
	    }
	}
	return new ModelAndView(new RedirectView(urlAccueil, true));
    }
}
