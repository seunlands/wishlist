package i2.application.extranet.action.accueil;

import java.util.List;

import i2.application.cerbere.commun.Profil;
import i2.application.extranet.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ChangerProfil implements Controller {

    private IAccueilHelper accueilHelper;

    private static String PARAM_PROFIL = "profils";

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	List<Profil> lstProfils = (List<Profil>) request.getSession().getAttribute(Constants.SES_PROFILS);
	int profilSel = Integer.valueOf(request.getParameter(PARAM_PROFIL));

	return accueilHelper.afficherAccueil(lstProfils, profilSel, null, request);
    }

    public void setAccueilHelper(IAccueilHelper accueilHelper) {
	this.accueilHelper = accueilHelper;
    }

}
