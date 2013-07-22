package i2.application.extranet.action.accueil;

import i2.application.aurige.bean.AutoEcole;
import i2.application.cerbere.commun.Cerbere;
import i2.application.cerbere.commun.Profil;
import i2.application.extranet.bean.ExTypeDroit;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.business.accueil.IProfilManager;
import i2.application.extranet.business.alertes.IAlertesManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.utils.Constants;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public class AccueilHelper implements IAccueilHelper {

    private String viewAccueil;
    private String viewErreurAE;
    private IAlertesManager alertesManager;
    private IProfilManager profilManager;

    @Override
    public ModelAndView afficherAccueil(List<Profil> lstProfils, int indexProfilSel, ViewUtilisateur userInSession, HttpServletRequest request) throws Exception {
	if (userInSession == null && lstProfils != null) {
	    Cerbere c = Cerbere.creation(request);

	    final Profil profilCerbere = lstProfils.get(indexProfilSel);
	    final String libelleProfilCerbere = profilCerbere.getNom();
	    final String restriction = profilCerbere.getRestriction();

	    ViewUtilisateur utilisateur = profilManager.getUtilisateurFromProfil(libelleProfilCerbere, restriction, c.getUtilisateur().getMel(), c.getUtilisateur().getNom(), c.getUtilisateur()
		    .getPrenom());

	    if (utilisateur == null) {
		request.getSession().setAttribute(Constants.SES_ERREUR, restriction);
		return new ModelAndView(viewErreurAE);
	    }

	    List<ExTypeDroit> droitsUser = profilManager.getListDroits(utilisateur.getLibelleProfil());

	    // Mise en session de ces droits
	    request.getSession().setAttribute(Constants.SES_DROITS, droitsUser);

	    // Mise en session de l'utilisateur
	    request.getSession().setAttribute(Constants.SES_UTILISATEUR, utilisateur);

	    // Mise en session de la liste de profils de l'utilisateur si +sieur et REP ou LECT
	    if (libelleProfilCerbere.equals(EnumProfils.LECTEUR.name()) || libelleProfilCerbere.equals(EnumProfils.REPARTITEUR.name())) {
		if (lstProfils.size() > 1) {
		    request.getSession().setAttribute(Constants.SES_PROFILS, lstProfils);
		    request.getSession().setAttribute(Constants.SES_PROFIL_SEL, indexProfilSel);
		}
	    }

	}
	// reccup nbalertes
	Long nbAlertes = 0L;
	userInSession = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userInSession != null && userInSession.getLibelleProfil().equals(EnumProfils.AE.getLibelle()) && userInSession.getRestriction() instanceof AutoEcole) {
	    nbAlertes = alertesManager.getNbAlertesAue((AutoEcole) userInSession.getRestriction());
	}
	if (userInSession != null && (userInSession.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()) || userInSession.getLibelleProfil().equals(EnumProfils.LECTEUR.getLibelle()))
		&& userInSession.getRestriction() instanceof AutoEcole) {
	    nbAlertes = alertesManager.getNbAlertesDept(((AutoEcole) userInSession.getRestriction()).getDepartement());
	}
	request.setAttribute(Constants.NB_ALERTES, nbAlertes);
	return new ModelAndView(viewAccueil);
    }

    public void setViewAccueil(String viewAccueil) {
	this.viewAccueil = viewAccueil;
    }

    public void setViewErreurAE(String viewErreurAE) {
	this.viewErreurAE = viewErreurAE;
    }

    public void setAlertesManager(IAlertesManager alertesManager) {
	this.alertesManager = alertesManager;
    }

    public void setProfilManager(IProfilManager profilManager) {
	this.profilManager = profilManager;
    }

}
