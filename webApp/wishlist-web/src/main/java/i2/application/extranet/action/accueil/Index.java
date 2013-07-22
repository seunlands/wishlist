package i2.application.extranet.action.accueil;

import i2.application.cerbere.commun.Cerbere;
import i2.application.cerbere.commun.Profil;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.utils.Constants;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class Index implements Controller {

    private IAccueilHelper accueilHelper;

    /**
     * Méthode appelée lors de l'accès à la page d'accueil
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	// Utilisateur actuel
	ViewUtilisateur userInSession = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	ArrayList<Profil> listeProfils = null;
	// Si null => initialisation
	if (userInSession == null) {

	    Cerbere c = Cerbere.creation(request);
	    // Récupération de la liste des profils (ADM,REP,LECT,AE...)
	    listeProfils = c.getHabilitation().getAllProfils();

	}
	return accueilHelper.afficherAccueil(listeProfils, 0, userInSession, request);
    }

    public void setAccueilHelper(IAccueilHelper accueilHelper) {
	this.accueilHelper = accueilHelper;
    }

}
