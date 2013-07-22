package i2.application.extranet.action.accueil;

import i2.application.cerbere.commun.Profil;
import i2.application.extranet.bean.view.ViewUtilisateur;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public interface IAccueilHelper {
    ModelAndView afficherAccueil(List<Profil> lstProfils, int indexProfilSel, ViewUtilisateur userInSession, HttpServletRequest request) throws Exception;
}
